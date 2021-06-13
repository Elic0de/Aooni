package com.elic0de.aooni;

import com.elic0de.aooni.command.Arguments;
import com.elic0de.aooni.command.Command;
import com.elic0de.aooni.command.Sender;
import com.elic0de.aooni.command.game.GameCommand;
import com.elic0de.aooni.command.game.GameEditCommand;
import com.elic0de.aooni.game.GameManager;
import com.elic0de.aooni.listeners.*;
import com.elic0de.aooni.user.UserSet;
import com.elic0de.aooni.user.selection.SelectionSet;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class Aooni extends JavaPlugin implements Listener {

    private static Aooni instance;
    private final HashMap<String, Command> commands = new HashMap<>();
    private InventoryManager manager;
    private final ArrayList<PlayerJoinListener> joinListeners = new ArrayList<>();
    private final ArrayList<PlayerQuitListener> quitListeners = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.instance = this;
        this.manager = new InventoryManager(this);
        this.manager.init();

        UserSet.load();
        GameManager.load();
        SelectionSet.load();

        registerEventListeners(
                new PlayerInteractListener(),
                new UserJoinListener(),
                new UserQuitListener(),
                UserSet.getInstnace(),
                SelectionSet.getInstance()
        );

        registerCommands(
                new GameCommand(),
                new GameEditCommand()
        );

        for(Player player : getServer().getOnlinePlayers()){
            PlayerJoinEvent event = new PlayerJoinEvent(player, "");
            for(PlayerJoinListener listener : joinListeners)
                listener.onJoin(event);
        }
    }

    public InventoryManager getManager() {
        return manager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        UserSet.getInstnace().saveAll();
        GameManager.getInstance().saveAll();

        for(Player player : getServer().getOnlinePlayers()){
            PlayerQuitEvent event = new PlayerQuitEvent(player, "");
            for(PlayerQuitListener listener : quitListeners)
                listener.onQuit(event);
        }
    }

    public static Aooni instance() {
        return instance;
    }

    private void registerEventListeners(Listener... listeners) {
        for(Listener listener : listeners){
            getServer().getPluginManager().registerEvents(listener, this);
            if(listener instanceof PlayerJoinListener) joinListeners.add((PlayerJoinListener) listener);
            if(listener instanceof PlayerQuitListener) quitListeners.add((PlayerQuitListener) listener);
        }
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
