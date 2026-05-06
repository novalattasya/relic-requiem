package com.relicrequiem.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RelicRequiemPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Relic Requiem Plugin aktif!");
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new RelicListener(), this);
        getServer().getPluginManager().registerEvents(new AchievementListener(), this);
        getServer().getPluginManager().registerEvents(new AwakeningListener(), this);
        getServer().getPluginManager().registerEvents(new OreListener(), this);
        new RelicEffectTask().runTaskTimer(this, 0L, 20L);

        long tigaJam = 20L * 60 * 60 * 3;
        getServer().getScheduler().runTaskTimer(this, MeteorManager::spawnRandomMeteor, tigaJam, tigaJam);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && player.isOp()) { // Onlyadmin
            
            if (command.getName().equalsIgnoreCase("getrelic")) {
                player.getInventory().addItem(RelicManager.createRelic());
                player.sendMessage("§aRelic dimanifestasikan");
                return true;
            } 
            
            else if (command.getName().equalsIgnoreCase("getmaterials")) {
                player.getInventory().addItem(MaterialManager.createFallenSoul());
                player.getInventory().addItem(MaterialManager.createWorldheartGem());
                player.getInventory().addItem(MaterialManager.createDominionCore());
                player.getInventory().addItem(MaterialManager.createWorldheartOre());
                player.sendMessage("§aBahan-bahan Awakening dimanifestasikan");
                return true;
            }

            else if (command.getName().equalsIgnoreCase("spawnmeteor")) {
                MeteorManager.spawnMeteorNearPlayer(player);
                return true;
            }
            
            // COMMAND BARU: BUAT RESET DATABASE BIAR BISA CRAFTING LAGI!
            else if (command.getName().equalsIgnoreCase("resetrelic")) {
                getConfig().set("relic_awoken", false);
                saveConfig();
                player.sendMessage("§a[!] Status Relic di Server telah di-reset! Kamu bisa crafting lagi sekarang.");
                return true;
            }
            
        } else {
            sender.sendMessage("§cBukan admin njir!");
            return true;
        }
        return false;
    }
}