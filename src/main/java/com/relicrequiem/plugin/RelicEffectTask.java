package com.relicrequiem.plugin;

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

                // EFEK 1: WALLHACK & SURVIVAL BUFF
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 1, false, false, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0, false, false, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0, false, false, false));

                // EFEK 2: MANHUNT LOCATOR BAR (TITIK MERAH VANILLA)
                if (!markedPlayers.contains(player.getUniqueId())) {
                    markedPlayers.add(player.getUniqueId());
                    
                    // KITA BUANG CUSTOM STYLE SIALAN ITU! Balik ke titik merah Vanilla yang 100% Work!
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "waypoint modify " + player.getName() + " color red");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "waypoint modify " + player.getName() + " style reset"); // Jaga-jaga bersihin cache error
                    
                    player.sendMessage("§4§l[!] AURA RELIC MELEDAK! Posisimu sekarang terekspos sebagai TITIK MERAH di Locator Bar!");
                }
            }
        }

        markedPlayers.removeIf(uuid -> {
            if (!currentHolders.contains(uuid)) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    // Reset warnanya ke default
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "waypoint modify " + p.getName() + " color reset");
                    
                    p.sendMessage("§a§l[!] Relic lepas dari tanganmu. Jejakmu di Locator Bar kembali tersembunyi.");
                }
                return true; 
            }
            return false;
        });
    }
}