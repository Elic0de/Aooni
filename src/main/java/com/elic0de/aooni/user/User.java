package com.elic0de.aooni.user;

import com.elic0de.aooni.config.Yaml;
import com.elic0de.aooni.scoreboard.StatusBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class User {

    //UUID
    public final UUID uuid;

    //ステータスボード
    public StatusBoard statusBoard;

    public User(Yaml yaml){
        //ファイル名に基づきUUIDを生成し代入する
        this.uuid = UUID.fromString(yaml.name);
    }

    public Optional<StatusBoard> statusBoard(){
        return Optional.of(statusBoard);
    }

    //このユーザーに対応したプレイヤーを取得する
    public Player asBukkitPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public void save() {
        Yaml yaml = UserSet.getInstnace().makeYaml(uuid);

        //セーブする
        yaml.save();
    }
}
