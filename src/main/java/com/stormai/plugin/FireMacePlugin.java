package com.stormai.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class FireMacePlugin extends JavaPlugin {
    private final List<MaceEffect> activeEffects = new ArrayList<>();
    private FireMace fireMace;
    private IceMace iceMace;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        fireMace = new FireMace(this);
        iceMace = new IceMace(this);
        getServer().getPluginManager().registerEvents(new MaceListener(this, fireMace, iceMace), this);
        getCommand("firemace").setExecutor(new ReloadCommand(this));
        getLogger().info("FireMace plugin enabled with Ice Mace support!");
    }

    @Override
    public void onDisable() {
        for (MaceEffect effect : new ArrayList<>(activeEffects)) {
            effect.cancel();
        }
        getLogger().info("FireMace plugin disabled!");
    }

    public void addActiveEffect(MaceEffect effect) {
        activeEffects.add(effect);
    }

    public void removeActiveEffect(MaceEffect effect) {
        activeEffects.remove(effect);
    }
}