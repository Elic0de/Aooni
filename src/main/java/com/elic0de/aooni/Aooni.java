package com.elic0de.aooni;

import com.elic0de.aooni.game.GameManager;
import com.elic0de.aooni.listeners.PlayerInteractListener;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Aooni extends JavaPlugin {

    private static Aooni instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.instance = this;

        GameManager.load();

        registerEventListeners(
                new PlayerInteractListener()
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
}
