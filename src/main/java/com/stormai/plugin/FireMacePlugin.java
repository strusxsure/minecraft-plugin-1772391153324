package com.stormai.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class FireMacePlugin extends JavaPlugin {
    private final List<ActiveFireMaceEffect> activeEffects = new ArrayList<>();
    private FireMace fireMace;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        fireMace = new FireMace(this);
        getServer().getPluginManager().registerEvents(new FireMaceListener(this, fireMace), this);
        getCommand("firemace").setExecutor(new ReloadCommand(this));
        getLogger().info("FireMace plugin enabled!");
    }

    @Override
    public void onDisable() {
        for (ActiveFireMaceEffect effect : new ArrayList<>(activeEffects)) {
            effect.cancel();
        }
        getLogger().info("FireMace plugin disabled!");
    }

    public void addActiveEffect(ActiveFireMaceEffect effect) {
        activeEffects.add(effect);
    }

    public void removeActiveEffect(ActiveFireMaceEffect effect) {
        activeEffects.remove(effect);
    }
}