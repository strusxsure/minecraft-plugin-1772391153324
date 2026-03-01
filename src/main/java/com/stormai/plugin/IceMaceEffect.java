package com.stormai.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class IceMaceEffect implements MaceEffect {
    private final FireMacePlugin plugin;
    private final Player player;
    private final Location center;
    private final int duration;
    private final double radius;
    private final int fallingBlockInterval;
    private final int blindnessDuration;
    private final int maxFreezeStacks;
    private final int freezeStackInterval;

    private BukkitRunnable task;
    private final Map<Block, Material> iceBlocks = new HashMap<>();
    private final Map<Player, Integer> freezeStacks = new HashMap<>();
    private final Set<Player> lastTickPlayers = new HashSet<>();
    private final Random random = new Random();
    private int fallingBlockTicker = 0;
    private int effectTicker = 0;

    public IceMaceEffect(FireMacePlugin plugin, Player player, Location center) {
        this.plugin = plugin;
        this.player = player;
        this.center = center;

        ConfigurationSection config = plugin.getConfig().getConfigurationSection("ice-mace");
        if (config == null) {
            config = plugin.getConfig();
        }

        this.duration = config.getInt("duration", 200);
        this.radius = config.getDouble("slippery-floor-radius", 5.0);
        this.fallingBlockInterval = config.getInt("falling-block-interval", 40);
        this.blindnessDuration = config.getInt("blindness-duration", 100);
        this.maxFreezeStacks = config.getInt("freeze-max-stacks", 5);
        this.freezeStackInterval = config.getInt("freeze-stack-interval", 20);
    }

    @Override
    public void start() {
        plugin.addActiveEffect(this);
        createSlipperyFloor();

        task = new BukkitRunnable() {
            int ticker = 0;

            @Override
            public void run() {
                ticker++;
                
                if (ticker >= duration) {
                    cancel();
                    return;
                }

                fallingBlockTicker++;
                effectTicker++;

                if (fallingBlockTicker >= fallingBlockInterval) {
                    triggerFallingBlocks();
                    fallingBlockTicker = 0;
                }

                if (effectTicker >= freezeStackInterval) {
                    applyBlindnessAndFreeze();
                    effectTicker = 0;
                }
            }
        };
        task.runTaskTimer(plugin, 0L, 1L);
    }

    private void createSlipperyFloor() {
        int groundY = center.getWorld().getHighestBlockYAt(center);
        Location groundCenter = new Location(center.getWorld(), center.getX(), groundY, center.getZ());
        
        for (int x = -(int)radius; x <= (int)radius; x++) {
            for (int z = -(int)radius; z <= (int)radius; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= radius) {
                    Block block = groundCenter.getBlock().getRelative(x, 0, z);
                    if (block.getType() != Material.AIR && 
                        block.getType() != Material.WATER && 
                        block.getType() != Material.LAVA &&
                        !block.isLiquid()) {
                        Material original = block.getType();
                        block.setType(Material.ICE);
                        iceBlocks.put(block, original);
                    }
                }
            }
        }
    }

    private void triggerFallingBlocks() {
        if (iceBlocks.isEmpty()) return;
        
        List<Block> blocks = new ArrayList<>(iceBlocks.keySet());
        if (!blocks.isEmpty()) {
            Block block = blocks.get(random.nextInt(blocks.size()));
            block.setType(Material.AIR);
            iceBlocks.remove(block);
            
            Block above = block.getRelative(0, 1, 0);
            if (above.getType() == Material.SAND || 
                above.getType() == Material.GRAVEL || 
                above.getType() == Material.RED_SAND) {
                above.setType(Material.AIR);
            }
        }
    }

    private void applyBlindnessAndFreeze() {
        Set<Player> currentPlayers = new HashSet<>();
        Collection<Entity> entities = center.getWorld().getNearbyEntities(center, radius, radius, radius);
        
        for (Entity entity : entities) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                currentPlayers.add(player);
                
                int currentStack = freezeStacks.getOrDefault(player, 0);
                if (currentStack < maxFreezeStacks) {
                    freezeStacks.put(player, currentStack + 1);
                }
                
                int stack = freezeStacks.get(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, blindnessDuration, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, blindnessDuration, Math.min(stack - 1, 10)));
            }
        }
        
        for (Player player : lastTickPlayers) {
            if (!currentPlayers.contains(player)) {
                freezeStacks.remove(player);
            }
        }
        
        lastTickPlayers.clear();
        lastTickPlayers.addAll(currentPlayers);
    }

    @Override
    public void cancel() {
        if (task != null) {
            task.cancel();
        }
        
        for (Map.Entry<Block, Material> entry : iceBlocks.entrySet()) {
            if (entry.getKey().getType() == Material.ICE) {
                entry.getKey().setType(entry.getValue());
            }
        }
        iceBlocks.clear();
        
        for (Player player : freezeStacks.keySet()) {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
            player.removePotionEffect(PotionEffectType.SLOWNESS);
        }
        freezeStacks.clear();
        lastTickPlayers.clear();
        
        plugin.removeActiveEffect(this);
    }
}