package com.elic0de.aooni;

import com.elic0de.aooni.command.Arguments;
import com.elic0de.aooni.command.Command;
import com.elic0de.aooni.command.Sender;
import com.elic0de.aooni.command.game.GameCommand;
import com.elic0de.aooni.command.game.GameEditCommand;
import com.elic0de.aooni.game.GameManager;
import com.elic0de.aooni.listeners.PlayerInteractListener;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Aooni extends JavaPlugin {

    private static Aooni instance;
    private final HashMap<String, Command> commands = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.instance = this;

        GameManager.load();

        registerEventListeners(
                new PlayerInteractListener()
        );

        registerCommands(
                new GameCommand(),
                new GameEditCommand()
        );

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        GameManager.getInstance().saveAll();
    }

    public static Aooni instance() {
        return instance;
    }

    private void registerEventListeners(Listener... listeners) {
        for (Listener listener : listeners)
            getServer().getPluginManager().registerEvents(listener, this);
    }

    private void registerCommands(Command... commands){
        for(Command command : commands){
            //コマンドのクラス名を取得する
            String className = command.getClass().getSimpleName();

            //接尾辞のCommandを削除し小文字化した物をコマンド名とする
            String commandName = className.substring(0, className.length() - 7).toLowerCase();
            //コマンド名とコマンドを結び付けて登録する
            this.commands.put(commandName, command);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args){

        commands.get(command.getName()).onCommand(new Sender(sender), new Arguments(args));
        return true;
    }
}
