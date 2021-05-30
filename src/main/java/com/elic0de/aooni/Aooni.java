package com.elic0de.aooni;

import com.elic0de.aooni.listener.PlayerInteractListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Aooni extends JavaPlugin {

    private static Aooni instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        registerEventListeners(
                new PlayerInteractListener()
        );

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Aooni instance() {
        return instance;
    }

    private void registerEventListeners(Listener... listeners) {
        for (Listener listener : listeners)
            getServer().getPluginManager().registerEvents(listener, this);
    }


}
