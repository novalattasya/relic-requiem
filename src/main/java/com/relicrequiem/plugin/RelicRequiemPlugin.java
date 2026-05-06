package com.relicrequiem.plugin;

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

        initializeWorldBorders();

        getServer().getPluginManager().registerEvents(new RelicListener(), this);
        getServer().getPluginManager().registerEvents(new AchievementListener(), this);
        getServer().getPluginManager().registerEvents(new AwakeningListener(), this);
        getServer().getPluginManager().registerEvents(new OreListener(), this);
        
        if (getServer().getPluginManager().getPlugin("MythicMobs") != null) {
            getServer().getPluginManager().registerEvents(new MythicDeathListener(), this);
        }

        new RelicEffectTask().runTaskTimer(this, 0L, 20L);

        long meteorInterval = 20L * 60 * 60 * 3;
        getServer().getScheduler().runTaskTimer(this, MeteorManager::spawnRandomMeteor, meteorInterval, meteorInterval);
    }

    private void initializeWorldBorders() {
        for (World world : Bukkit.getWorlds()) {
            WorldBorder border = world.getWorldBorder();
            border.setCenter(0.0, 0.0);
            border.setSize(8000.0);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player) || !player.isOp()) {
            sender.sendMessage("§cInsufficient permissions.");
            return true;
        }

        String cmdName = command.getName().toLowerCase();
        
        switch (cmdName) {
            case "getrelic":
                player.getInventory().addItem(RelicManager.createRelic());
                player.sendMessage("§aRelic item generated.");
                return true;
            
            case "getmaterials":
                player.getInventory().addItem(MaterialManager.createFallenSoul());
                player.getInventory().addItem(MaterialManager.createWorldheartGem());
                player.getInventory().addItem(MaterialManager.createDominionCore());
                player.getInventory().addItem(MaterialManager.createWorldheartOre());
                player.sendMessage("§aAwakening materials generated.");
                return true;

            case "spawnmeteor":
                MeteorManager.spawnMeteorNearPlayer(player);
                return true;
            
            case "resetrelic":
                getConfig().set("relic_awoken", false);
                saveConfig();
                player.sendMessage("§a[!] Server relic status has been reset.");
                return true;

            default:
                return false;
        }
    }
}