package com.elic0de.aooni.scoreboard;

import com.elic0de.aooni.enums.MatchState;
import com.elic0de.aooni.game.Game;
import com.elic0de.aooni.util.format.TimeFormat;
import com.elic0de.aooni.util.tuplet.Triple;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class StatusBoard {

    private static class Line extends Triple<Integer, String, Function<Game, Object>> {

        public Line(Integer score, String text, Function<Game, Object> value) {
            super(score, text, value);
        }

    }

    private static final List<Line> LINES;
    private static final List<Line> WAITING_LINES;
    private static final List<Line> WAITING_START_LINES;
    private static final Pattern DOUBLE_BYTE_CHARACTER_CHECKER = Pattern.compile("^[^!-~｡-ﾟ]+$");

    static{
        LINES = ImmutableList.of(
                new Line(3, "&e残り人数 $value", u -> u.getPlayers().size()),
                new Line(2, "&6残り時間 $value", u -> TimeFormat.secondsToTimeString(u.getTimeLimit())),
                new Line(1, " ", u -> ""),
                new Line(0, "&ehatosaba.f5.si", u -> "")

        );

        WAITING_LINES = ImmutableList.of(
                new Line(8, "   ", u -> ""),
                new Line(7, "マップ名: &a$value", u -> u.getName()),
                new Line(6, "プレイヤー数: &a$value", u -> u.getPlayers().size() + "/" + u.getMaxPlayers()),
                new Line(5, "  ", u -> ""),
                new Line(4, "ゲームをはじめるには", u -> ""),
                new Line(3, "あと&a$value&rプレイヤー", u -> u.getMinPlayers() - u.getPlayers().size()),
                new Line(2, "が必要です", u -> ""),
                new Line(1, " ", u -> ""),
                new Line(0, "&ehatosaba.f5.si", u -> "")
        );

        WAITING_START_LINES = ImmutableList.of(
                new Line(3, "はじまるまで", u -> ""),
                new Line(2, "残り&a$value&r秒", u -> u.getTimer()),
                new Line(1, " ", u -> ""),
                new Line(0, "&ehatosaba.f5.si", u -> "")
        );
    }

    private final Player player;
    private final Game game;
    private Scoreboard board;

    public StatusBoard(Player player, Game game){
        this.player = player;
        this.game = game;
    }

    public void loadScoreboard(){
        clearScoreboard();
        //スコアボードを新しく作成する
        board = new Scoreboard(player);

        for(Line line : getLine()){
            //表示するテキストを作成する
            String text = ChatColor.translateAlternateColorCodes('&', line.second.replace("$value", line.third.apply(game).toString()));
            //タイトルを設定
            board.setTitle("Aooni");

            //対応したスコアにテキストをセットする
            board.setScore(line.first, text);
        }

        board.setDisplay(true);
    }

    public void clearScoreboard(){
        if(board == null) return;

        board.setDisplay(false);

        board = null;
    }

    public void updateAll(){
        for(int score = 0; score < getLine().size() - 1; score++) updateValue(score, false);
    }

    private void updateValue(int score){
        updateValue(score, false);
    }

    private void updateValue(int score, boolean whetherToUpdate){
        if(board == null) return;

        Line line = getLine().get(score);

        //現在表示されている文字列を取得する
        String before = board.getScore(score);

        Player player = this.player;

        //表示する文字列を作成する
        String after = line.second.replace("$value", line.third.apply(game).toString());

        //指定されたスコアをアップデートする
        board.updateScore(line.first, after);

        //サーバーアドレスの表示を更新しないのであれば戻る
        if(!whetherToUpdate) return;

        //board.updateScore(serverAddress.second, serverAddress);
    }

    private List<Line> getLine() {
        if (game.getMatchState() == MatchState.WAITINGLOBBY) {
            return WAITING_LINES;
        } else if(game.getMatchState() == MatchState.WAITINGSTART){
            return WAITING_START_LINES;
        } else if(game.getMatchState() == MatchState.PLAYING){
            return LINES;
        }
        return LINES;
    }

}
