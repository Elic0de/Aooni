package com.elic0de.aooni.listeners;

import com.elic0de.aooni.user.User;
import com.elic0de.aooni.user.UserSet;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class UserJoinListener implements PlayerJoinListener {

    private final UserSet users = UserSet.getInstnace();

    @Override
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage("testtest");
    }
}
