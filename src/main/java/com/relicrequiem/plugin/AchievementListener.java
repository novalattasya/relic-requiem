package com.relicrequiem.plugin;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class AchievementListener implements Listener {

    private final RelicRequiemPlugin plugin = JavaPlugin.getPlugin(RelicRequiemPlugin.class);

    // Kunci memori di badan player
    private final NamespacedKey HAS_SOUL = new NamespacedKey(plugin, "achv_has_soul");
    private final NamespacedKey HAS_GEM = new NamespacedKey(plugin, "achv_has_gem");
    private final NamespacedKey HAS_CORE = new NamespacedKey(plugin, "achv_has_core");

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack item = event.getItem().getItemStack();

        if (MaterialManager.isFallenSoul(item) && !player.getPersistentDataContainer().has(HAS_SOUL, PersistentDataType.BYTE)) {
            player.getPersistentDataContainer().set(HAS_SOUL, PersistentDataType.BYTE, (byte) 1);
            Bukkit.broadcastMessage("§d§l[ACHIEVEMENT] §e" + player.getName() + " telah menemukan pecahan jiwa... §8[Fallen Soul]");
        }
        else if (MaterialManager.isWorldheartGem(item) && !player.getPersistentDataContainer().has(HAS_GEM, PersistentDataType.BYTE)) {
            player.getPersistentDataContainer().set(HAS_GEM, PersistentDataType.BYTE, (byte) 1);
            Bukkit.broadcastMessage("§d§l[ACHIEVEMENT] §e" + player.getName() + " menggali rahasia inti bumi... §8[Worldheart Gem]");
        }
        else if (MaterialManager.isDominionCore(item) && !player.getPersistentDataContainer().has(HAS_CORE, PersistentDataType.BYTE)) {
            player.getPersistentDataContainer().set(HAS_CORE, PersistentDataType.BYTE, (byte) 1);
            Bukkit.broadcastMessage("§d§l[ACHIEVEMENT] §e" + player.getName() + " merebut kekuatan sang penguasa! §8[Dominion Core]");
        }
    }
}