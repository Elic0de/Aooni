package com.elic0de.aooni.command;

import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public interface Command extends Listener {

    List<String> aliases = new ArrayList<>();

    void onCommand(Sender sender, Arguments args);

    default boolean blockNonPlayer(Sender sender){
        if(sender.isPlayerCommandSender()) return false;

        sender.warn("ゲーム内から実行して下さい。");
        return true;
    }

    default boolean hasPermission(Sender sender, String commandName){
        if (sender.hasPermission("aooni." + commandName.toLowerCase())) return false;
        sender.sendMessage("このコマンドを実行する権限がありません");
        return true;
    }

    default List<String> get() {
        return aliases;
    }
}
