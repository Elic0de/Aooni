package com.elic0de.aooni.listeners;

import com.elic0de.aooni.user.User;
import com.elic0de.aooni.user.UserSet;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserQuitListener implements PlayerQuitListener {

    @Override
    public void onQuit(PlayerQuitEvent event) {
        User user = UserSet.getInstnace().getUser(event.getPlayer());
        Player player = event.getPlayer();

        user.statusBoard = null;


    }
}
