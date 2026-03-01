package com.stormai.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand implements CommandExecutor {
    private final FireMacePlugin plugin;

    public ReloadCommand(FireMacePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            return false;
        }
        if (!sender.hasPermission("firemace.reload")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }
        plugin.reloadConfig();
        plugin.saveConfig();
        sender.sendMessage("§aFireMace configuration reloaded!");
        return true;
    }
}