package com.elic0de.aooni.game;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;

import com.elic0de.aooni.Aooni;
import com.elic0de.aooni.config.Yaml;

public class GameManager {

    private static GameManager instance;

    public static void load(){
        instance = new GameManager();
    }

    public static GameManager getInstance(){
        return instance;
    }

    private final Aooni plugin = Aooni.instance();

    //マップデータを保存するフォルダー
    public final File folder = new File(plugin.getDataFolder() + File.separator + "maps");

    //ゲームのマップ
    private final Map<String, Game> games = new HashMap<>();

    private GameManager(){
        //フォルダーが存在しなければ作成する
        if(!folder.exists()) folder.mkdirs();

        //各アスレコンフィグ毎に処理をする
        for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[0])){
            String fileName = file.getName();

            //拡張子を削除してゲーム名を取得する
            String gameName = fileName.substring(0, fileName.length() - 4);

            //ゲームを登録する
            registerGame(gameName);
        }
    }

    public void saveAll(){
        games.values().forEach(Game::save);
    }

    public void registerGame(String gameName){
        File file = new File(folder, gameName + ".yml");

        //コンフィグが存在しなければ戻る
        if(!file.exists()) return;

        //コンフィグを取得する
        Yaml yaml = makeYaml(gameName);

        //コンフィグに基づきアスレを生成する
        Game game =  new Game(this, yaml);
        games.put(game.getName(), game);
    }


    public Collection<Game> getGames(){
        return games.values();
    }

    public Stream<Game> getEnabledGames(){
        return games.values().stream()
                .filter(parkour -> parkour.isEnable());
    }

    public Game getGame(String gameName){
        return games.get(gameName);
    }

    public boolean containsGame(Game game){
        return containsGame(game.getName());
    }

    public boolean containsGame(String gameName){
        return games.containsKey(gameName);
    }

    public Yaml makeYaml(String gameName){
        return new Yaml(plugin, new File(folder, gameName + ".yml"), "game.yml");
    }
}
