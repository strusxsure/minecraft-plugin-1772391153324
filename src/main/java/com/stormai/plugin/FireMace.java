package com.stormai.plugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FireMace {
    private final FireMacePlugin plugin;

    public FireMace(FireMacePlugin plugin) {
        this.plugin = plugin;
    }

    public void activate(Player player, Location location) {
        new ActiveFireMaceEffect(plugin, player, location).start();
    }
}