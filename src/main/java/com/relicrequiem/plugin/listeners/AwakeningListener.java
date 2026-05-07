package com.relicrequiem.plugin.listeners;

import com.relicrequiem.plugin.RelicRequiemPlugin;
import com.relicrequiem.plugin.config.ConfigManager;
import com.relicrequiem.plugin.managers.MaterialManager;
import com.relicrequiem.plugin.managers.RelicManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public class AwakeningListener implements Listener {

    private final RelicRequiemPlugin plugin = JavaPlugin.getPlugin(RelicRequiemPlugin.class);

    @EventHandler
    public void onAwaken(PlayerInteractEvent event) {
        ConfigManager config = ConfigManager.getInstance();
        Player player = event.getPlayer();

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.isSneaking()) {
            ItemStack handItem = event.getItem();

            if (MaterialManager.isDominionCore(handItem)) {
                event.setCancelled(true);

                if (config.isRelicAwakened()) {
                    player.sendMessage(config.getRelicAlreadyAwoken());
                    return;
                }

                boolean hasSoul = false;
                boolean hasGem = false;

                for (ItemStack item : player.getInventory().getContents()) {
                    if (MaterialManager.isFallenSoul(item)) hasSoul = true;
                    if (MaterialManager.isWorldheartGem(item)) hasGem = true;
                }

                if (hasSoul && hasGem) {
                    removeMaterial(player, MaterialManager.FALLEN_SOUL_KEY);
                    removeMaterial(player, MaterialManager.WORLDHEART_GEM_KEY);
                    removeMaterial(player, MaterialManager.DOMINION_CORE_KEY);

                    player.getInventory().addItem(RelicManager.createRelic());

                    config.setRelicAwakened(true);

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0f, 0.5f);
                        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);

                        Title title = Title.title(
                                Component.text("THE RELIC AWAKENS").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD),
                                Component.text(player.getName() + " has claimed the power!").color(NamedTextColor.GOLD),
                                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(6), Duration.ofSeconds(2))
                        );
                        p.showTitle(title);

                        String message = config.getAwakeningBroadcast().replace("%player%", player.getName());
                        p.sendMessage(message);
                    }

                    player.sendMessage(config.getAwakeningPersonal());
                } else {
                    player.sendMessage(config.getMissingMaterials());
                }
            }
        }
    }

    private void removeMaterial(Player player, NamespacedKey key) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
                item.setAmount(item.getAmount() - 1);
                break;
            }
        }
    }
}