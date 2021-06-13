package com.elic0de.aooni.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public interface PlayerQuitListener extends Listener {

    @EventHandler
    void onQuit(PlayerQuitEvent event);

}
