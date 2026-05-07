package com.relicrequiem.plugin;

import com.relicrequiem.plugin.config.ConfigManager;
import com.relicrequiem.plugin.listeners.*;
import com.relicrequiem.plugin.managers.*;
import com.relicrequiem.plugin.tasks.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RelicRequiemPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Relic Requiem Plugin is enabled.");
        saveDefaultConfig();

        ConfigManager.initialize(this);

        initializeWorldBorders();

        getServer().getPluginManager().registerEvents(new RelicListener(), this);
        getServer().getPluginManager().registerEvents(new AchievementListener(), this);
        getServer().getPluginManager().registerEvents(new AwakeningListener(), this);
        getServer().getPluginManager().registerEvents(new OreListener(), this);
        
        if (getServer().getPluginManager().getPlugin("MythicMobs") != null) {
            getServer().getPluginManager().registerEvents(new MythicDeathListener(), this);
        }

        new RelicEffectTask().runTaskTimer(this, 0L, 20L);

        long meteorInterval = 20L * 60 * 60 * ConfigManager.getInstance().getMeteorSpawnIntervalHours();
        getServer().getScheduler().runTaskTimer(this, MeteorManager::spawnRandomMeteor, meteorInterval, meteorInterval);
    }

    private void initializeWorldBorders() {
        double borderSize = ConfigManager.getInstance().getWorldBorderSize();
        for (World world : Bukkit.getWorlds()) {
            WorldBorder border = world.getWorldBorder();
            border.setCenter(0.0, 0.0);
            border.setSize(borderSize);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ConfigManager config = ConfigManager.getInstance();
        
        if (!(sender instanceof Player player) || !player.isOp()) {
            sender.sendMessage(config.getInsufficientPermissions());
            return true;
        }

        String cmdName = command.getName().toLowerCase();
        
        switch (cmdName) {
            case "getrelic":
                player.getInventory().addItem(RelicManager.createRelic());
                player.sendMessage(config.getRelicCraftingSuccess());
                return true;
            
            case "getmaterials":
                player.getInventory().addItem(MaterialManager.createFallenSoul());
                player.getInventory().addItem(MaterialManager.createWorldheartGem());
                player.getInventory().addItem(MaterialManager.createDominionCore());
                player.getInventory().addItem(MaterialManager.createWorldheartOre());
                player.sendMessage(config.getMaterialsGenerated());
                return true;

            case "spawnmeteor":
                MeteorManager.spawnMeteorNearPlayer(player);
                player.sendMessage(config.getAdminMeteorSpawned());
                return true;
            
            case "resetrelic":
                config.setRelicAwakened(false);
                player.sendMessage(config.getAdminRelicReset());
                return true;
            
            case "relic":
                if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                    return handleReloadCommand(player, config);
                }
                return false;

            default:
                return false;
        }
    }

    private boolean handleReloadCommand(Player player, ConfigManager config) {
        try {
            player.sendMessage(config.getAdminReloadStart());
            config.reload();
            player.sendMessage(config.getAdminReloadSuccess());
            getLogger().info("Config reloaded by " + player.getName());
            return true;
        } catch (Exception e) {
            player.sendMessage(config.getAdminReloadFailed());
            getLogger().severe("Failed to reload config: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }
}