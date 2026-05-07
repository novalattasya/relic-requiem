package com.relicrequiem.plugin.tasks;

import com.relicrequiem.plugin.config.ConfigManager;
import com.relicrequiem.plugin.managers.RelicManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RelicEffectTask extends BukkitRunnable {

    private final Set<UUID> markedPlayers = new HashSet<>();

    @Override
    public void run() {
        ConfigManager config = ConfigManager.getInstance();
        Set<UUID> currentHolders = new HashSet<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            boolean hasRelic = false;

            for (ItemStack item : player.getInventory().getContents()) {
                if (RelicManager.isRelic(item)) {
                    hasRelic = true;
                    break;
                }
            }

            if (hasRelic) {
                currentHolders.add(player.getUniqueId());

                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 1, false, false, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0, false, false, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0, false, false, false));

                if (!markedPlayers.contains(player.getUniqueId())) {
                    markedPlayers.add(player.getUniqueId());
                    
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "waypoint modify " + player.getName() + " color red");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "waypoint modify " + player.getName() + " style reset");
                    
                    player.sendMessage(config.getRelicEffectMarked());
                }
            }
        }

        markedPlayers.removeIf(uuid -> {
            if (!currentHolders.contains(uuid)) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "waypoint modify " + p.getName() + " color reset");
                }
                return true;
            }
            return false;
        });
    }
}