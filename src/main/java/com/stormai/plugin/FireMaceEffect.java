package com.stormai.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireMaceEffect implements MaceEffect {
    private final FireMacePlugin plugin;
    private final Player player;
    private final Location center;
    private final int duration;
    private final int damageInterval;
    private final double damageAmount;
    private final double radius;
    private final int mobSpawnInterval;
    private final List<EntityType> mobTypes;
    private final boolean flameWallEnabled;
    private final int flameWallLength;
    private final int flameWallHeight;
    private final boolean lavaTrapEnabled;
    private final int lavaTrapDuration;

    private BukkitRunnable task;
    private final List<Block> placedLava = new ArrayList<>();
    private final List<Block> placedFlameWall = new ArrayList<>();
    private final Random random = new Random();

    public FireMaceEffect(FireMacePlugin plugin, Player player, Location center) {
        this.plugin = plugin;
        this.player = player;
        this.center = center;

        ConfigurationSection config = plugin.getConfig().getConfigurationSection("fire-mace");
        if (config == null) {
            config = plugin.getConfig();
        }

        this.duration = config.getInt("duration", 200);
        this.damageInterval = config.getInt("damage-interval", 20);
        this.damageAmount = config.getDouble("damage-amount", 2.0);
        this.radius = config.getDouble("damage-radius", 5.0);
        this.mobSpawnInterval = config.getInt("mob-spawn-interval", 40);
        this.mobTypes = new ArrayList<>();
        for (String typeName : config.getStringList("mob-types")) {
            try {
                EntityType type = EntityType.valueOf(typeName);
                mobTypes.add(type);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid mob type in config: " + typeName);
            }
        }
        ConfigurationSection flameWallConfig = config.getConfigurationSection("flame-wall");
        if (flameWallConfig != null) {
            this.flameWallEnabled = flameWallConfig.getBoolean("enabled", true);
            this.flameWallLength = flameWallConfig.getInt("length", 5);
            this.flameWallHeight = flameWallConfig.getInt("height", 3);
        } else {
            this.flameWallEnabled = false;
            this.flameWallLength = 0;
            this.flameWallHeight = 0;
        }
        ConfigurationSection lavaTrapConfig = config.getConfigurationSection("lava-trap");
        if (lavaTrapConfig != null) {
            this.lavaTrapEnabled = lavaTrapConfig.getBoolean("enabled", true);
            this.lavaTrapDuration = lavaTrapConfig.getInt("duration", 80);
        } else {
            this.lavaTrapEnabled = false;
            this.lavaTrapDuration = 0;
        }
    }

    @Override
    public void start() {
        plugin.addActiveEffect(this);

        if (lavaTrapEnabled) {
            placeLavaTrap();
        }
        if (flameWallEnabled) {
            placeFlameWall();
        }

        task = new BukkitRunnable() {
            int damageTicker = 0;
            int mobSpawnTicker = 0;

            @Override
            public void run() {
                damageTicker++;
                mobSpawnTicker++;

                if (damageTicker >= duration) {
                    cancel();
                    return;
                }

                if (damageTicker % damageInterval == 0) {
                    applyHeatDamage();
                }

                if (mobSpawnTicker % mobSpawnInterval == 0) {
                    spawnMob();
                }
            }
        };
        task.runTaskTimer(plugin, 0L, 1L);
    }

    private void placeLavaTrap() {
        Block block = center.getBlock();
        if (block.getType() == Material.AIR || block.isLiquid()) {
            block.setType(Material.LAVA);
            placedLava.add(block);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (block.getType() == Material.LAVA) {
                    block.setType(Material.AIR);
                }
                placedLava.remove(block);
            }, lavaTrapDuration);
        }
    }

    private void placeFlameWall() {
        Location loc = center.clone();
        Vector direction = player.getLocation().getDirection().normalize();
        for (int i = 0; i < flameWallLength; i++) {
            for (int j = 0; j < flameWallHeight; j++) {
                Location blockLoc = loc.clone().add(direction.clone().multiply(i)).add(0, j, 0);
                Block block = blockLoc.getBlock();
                if (block.getType() == Material.AIR) {
                    block.setType(Material.FIRE);
                    placedFlameWall.add(block);
                }
            }
        }
        int flameWallDuration = 100;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Block block : placedFlameWall) {
                if (block.getType() == Material.FIRE) {
                    block.setType(Material.AIR);
                }
            }
            placedFlameWall.clear();
        }, flameWallDuration);
    }

    private void applyHeatDamage() {
        for (Entity entity : center.getWorld().getNearbyEntities(center, radius, radius, radius)) {
            if (entity instanceof LivingEntity living) {
                living.damage(damageAmount);
                living.setFireTicks(100);
            }
        }
    }

    private void spawnMob() {
        if (mobTypes.isEmpty()) return;
        EntityType type = mobTypes.get(random.nextInt(mobTypes.size()));
        double x = center.getX() + (random.nextDouble() * 2 - 1) * radius;
        double y = center.getY() + random.nextDouble() * 2;
        double z = center.getZ() + (random.nextDouble() * 2 - 1) * radius;
        Location loc = new Location(center.getWorld(), x, y, z);
        if (loc.getBlock().getType() == Material.AIR) {
            loc = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0, 1, 0);
        }
        center.getWorld().spawnEntity(loc, type);
    }

    @Override
    public void cancel() {
        if (task != null) {
            task.cancel();
        }
        for (Block block : placedLava) {
            if (block.getType() == Material.LAVA) {
                block.setType(Material.AIR);
            }
        }
        placedLava.clear();
        for (Block block : placedFlameWall) {
            if (block.getType() == Material.FIRE) {
                block.setType(Material.AIR);
            }
        }
        placedFlameWall.clear();
        plugin.removeActiveEffect(this);
    }
}