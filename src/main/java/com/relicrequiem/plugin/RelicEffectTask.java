package com.relicrequiem.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class RelicEffectTask extends BukkitRunnable {

    @Override
    public void run() {
        // Cek semua player yang sedang online
        for (Player player : Bukkit.getOnlinePlayers()) {
            boolean hasRelic = false;

            // Cek isi tas mereka satu per satu
            for (ItemStack item : player.getInventory().getContents()) {
                if (RelicManager.isRelic(item)) {
                    hasRelic = true;
                    break; // Udah ketemu Relic-nya, gak usah cek slot lain
                }
            }

            // Jika player ini bawa Relic, kasih buff
            if (hasRelic) {
                // Durasi 60 Ticks = 3 Detik. 
                // PotionEffect(Type, Duration, Amplifier, Ambient, Particles, Icon)
                // Amplifier 1 = Level 2 (Resistance II mengurangi 40% damage)
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 1, false, false, true));
                
                // Amplifier 0 = Level 1 (Regeneration I)
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0, false, false, true));
            }
        }
    }
}