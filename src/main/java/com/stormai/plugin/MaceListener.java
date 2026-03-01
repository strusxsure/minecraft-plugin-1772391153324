package com.stormai.plugin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MaceListener implements Listener {
    private final FireMacePlugin plugin;
    private final FireMace fireMace;
    private final IceMace iceMace;

    public MaceListener(FireMacePlugin plugin, FireMace fireMace, IceMace iceMace) {
        this.plugin = plugin;
        this.fireMace = fireMace;
        this.iceMace = iceMace;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;

        if (item.getType() == Material.MACE && item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
            int customModelData = item.getItemMeta().getCustomModelData();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block targetBlock = event.getClickedBlock();
                if (targetBlock != null) {
                    Location targetLocation = targetBlock.getLocation().add(0.5, 0.5, 0.5);
                    if (customModelData == 1) {
                        fireMace.activate(player, targetLocation);
                        event.setCancelled(true);
                    } else if (customModelData == 2) {
                        iceMace.activate(player, targetLocation);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}