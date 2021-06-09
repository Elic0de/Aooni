package com.elic0de.aooni.game;

import com.elic0de.aooni.Aooni;
import com.elic0de.aooni.config.Yaml;
import com.elic0de.aooni.enums.MatchState;
import com.elic0de.aooni.utilities.location;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Game {

    private final GameManager gameManager;

    // プレイヤーデータ
    private List<Player> players = new ArrayList<>();
    private List<Player> aooniPlayers = new ArrayList<>();
    private List<Player> spectators = new ArrayList<>();
    private List<Player> winners = new ArrayList<>();
    private final HashMap<Player, Integer> playerKills = new HashMap<>();

    //ゲームデータ
    private String name;
    private boolean enable;
    private MatchState matchState;

    private int minPlayers;
    private int maxPlayers;

    private String currentTime;
    private int timeLimit;
    private  int timer;

    private Location spawn;
    private Location area;


    public  Game(GameManager games, Yaml yaml){
        this.gameManager = games;
        //yaml.nameは拡張子を取り除いたファイル名を返すのでゲーム名としてそのまま設定する
        this.name = yaml.name;
        this.enable = yaml.getBoolean("Enable");
        this.matchState = MatchState.WAITINGLOBBY;
        this.minPlayers = yaml.getInt("minPlayers");
        this.maxPlayers = yaml.getInt("maxPlayers");
        this.timeLimit = yaml.getInt("timeLimit");
        this.timer = yaml.getInt("timer");
        this.spawn = location.stringToLocation(yaml.getString("spawn"));
        this.area = location.stringToLocation(yaml.getString("area"));
    }

    public String getName() {
        return name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void broadcast(String msg){
        players.forEach(player -> player.sendMessage(msg));
    }

    public void joinGame(Player player){
        MatchState matchState = this.matchState;

        /*if(players.size() >= this.maxPlayers) {
            player.sendMessage("満員のためゲームに参加できません");
        }*/

        if(matchState.equals(MatchState.PLAYING)) {
            spectators.add(player);
            player.sendMessage("スペクテイターとして参加しました");
        }else {
            players.add(player);
            player.sendMessage("ゲームに参加しました");
        }

        if (this.minPlayers >= this.players.size()) waitStart();

        teleportToGameLobby(player);
    }

    public void leaveGame(Player player){
        players.remove(player);
    }

    public void teleportToArea(Player player){
        player.teleport(area);
    }

    public void teleportToGameLobby(Player player){
        player.teleport(spawn);
    }

    public void waitStart(){
        this.matchState = MatchState.WAITINGSTART;
        //残り3秒後に鬼を決める
        new BukkitRunnable(){

            @Override
            public void run(){
                timer--;
                if(timer <= 0){
                    cancel();
                    startGame();
                }else {
                    if(timer == 3) {
                        Player player = players.get(new Random(players.size()).nextInt());
                        aooniPlayers.add(player);
                        broadcast("今回の鬼は" + player.getDisplayName() + "です");
                    }
                    players.forEach(player -> playSound(player, Sound.BLOCK_LEVER_CLICK));
                    broadcast("はじまるまで" + timer + "秒前");
                }
            }
        }.runTaskTimer(Aooni.instance(), 0L, 20L);
    }

    public void startGame(){
        this.matchState = MatchState.PLAYING;
        this.players.stream().filter(player -> !aooniPlayers.stream().anyMatch(oni -> player.equals(oni))).collect(Collectors.toList()).forEach(player -> teleportToArea(player));

        //chestをセットする

        players.forEach(player -> playSound(player, Sound.ENTITY_PLAYER_LEVELUP));
        broadcast("スタート！");

        new BukkitRunnable() {

            @Override
            public void run() {
                timeLimit --;
                if(timeLimit <= 0){
                    cancel();
                    broadcast("終了!!");
                    doDisplayResult();
                }
            }
        }.runTaskTimer(Aooni.instance(), 0L, 20L);
    }

    public void endGame(){
        this.matchState = MatchState.ENDING;
        players.clear();
        winners.clear();
        spectators.clear();
    }

    private void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0f, 2.0f);
    }

    public void doDisplayResult() {

    }

    public void won(Player player){
        winners.add(player);
    }

    public void update(){

    }

    public void save(){

    }
}
