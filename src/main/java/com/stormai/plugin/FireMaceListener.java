package com.stormai.plugin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FireMaceListener implements Listener {
    private final FireMacePlugin plugin;
    private final FireMace fireMace;

    public FireMaceListener(FireMacePlugin plugin, FireMace fireMace) {
        this.plugin = plugin;
        this.fireMace = fireMace;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;

        if (item.getType() == Material.MACE && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 1) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block targetBlock = event.getClickedBlock();
                if (targetBlock != null) {
                    fireMace.activate(player, targetBlock.getLocation().add(0.5, 0.5, 0.5));
                    event.setCancelled(true);
                }
            }
        }
    }
}