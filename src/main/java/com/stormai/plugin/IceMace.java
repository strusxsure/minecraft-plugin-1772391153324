package com.stormai.plugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class IceMace {
    private final FireMacePlugin plugin;

    public IceMace(FireMacePlugin plugin) {
        this.plugin = plugin;
    }

    public void activate(Player player, Location location) {
        new IceMaceEffect(plugin, player, location).start();
    }
}