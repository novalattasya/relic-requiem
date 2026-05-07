package com.relicrequiem.plugin.listeners;

import com.relicrequiem.plugin.RelicRequiemPlugin;
import com.relicrequiem.plugin.config.ConfigManager;
import com.relicrequiem.plugin.managers.MaterialManager;
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

    private final NamespacedKey HAS_SOUL = new NamespacedKey(plugin, "achv_has_soul");
    private final NamespacedKey HAS_GEM = new NamespacedKey(plugin, "achv_has_gem");
    private final NamespacedKey HAS_CORE = new NamespacedKey(plugin, "achv_has_core");

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        ConfigManager config = ConfigManager.getInstance();
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack item = event.getItem().getItemStack();

        if (MaterialManager.isFallenSoul(item) && !player.getPersistentDataContainer().has(HAS_SOUL, PersistentDataType.BYTE)) {
            player.getPersistentDataContainer().set(HAS_SOUL, PersistentDataType.BYTE, (byte) 1);
            String message = config.getAchievementFallenSoul().replace("%player%", player.getName());
            Bukkit.broadcastMessage(message);
        }
        else if (MaterialManager.isWorldheartGem(item) && !player.getPersistentDataContainer().has(HAS_GEM, PersistentDataType.BYTE)) {
            player.getPersistentDataContainer().set(HAS_GEM, PersistentDataType.BYTE, (byte) 1);
            String message = config.getAchievementWorldheartGem().replace("%player%", player.getName());
            Bukkit.broadcastMessage(message);
        }
        else if (MaterialManager.isDominionCore(item) && !player.getPersistentDataContainer().has(HAS_CORE, PersistentDataType.BYTE)) {
            player.getPersistentDataContainer().set(HAS_CORE, PersistentDataType.BYTE, (byte) 1);
            String message = config.getAchievementDominionCore().replace("%player%", player.getName());
            Bukkit.broadcastMessage(message);
        }
    }
}