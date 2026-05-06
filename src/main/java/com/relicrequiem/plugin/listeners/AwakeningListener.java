package com.relicrequiem.plugin.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public class AwakeningListener implements Listener {

    private final RelicRequiemPlugin plugin = JavaPlugin.getPlugin(RelicRequiemPlugin.class);

    @EventHandler
    public void onAwaken(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.isSneaking()) {
            ItemStack handItem = event.getItem();

            if (MaterialManager.isDominionCore(handItem)) {
                event.setCancelled(true);

                if (plugin.getConfig().getBoolean("relic_awoken", false)) {
                    player.sendMessage("§c[!] The Worldheart Relic telah dibangkitkan oleh seseorang. Kesempatan ini sudah hilang!");
                    return;
                }

                boolean hasSoul = false;
                boolean hasGem = false;

                for (ItemStack item : player.getInventory().getContents()) {
                    if (MaterialManager.isFallenSoul(item)) hasSoul = true;
                    if (MaterialManager.isWorldheartGem(item)) hasGem = true;
                }

                if (hasSoul && hasGem) {
                    // Awakening
                    // 1. Hapus bahan-bahannya
                    removeMaterial(player, MaterialManager.FALLEN_SOUL_KEY);
                    removeMaterial(player, MaterialManager.WORLDHEART_GEM_KEY);
                    removeMaterial(player, MaterialManager.DOMINION_CORE_KEY);

                    // 2. Berikan Relic
                    player.getInventory().addItem(RelicManager.createRelic());

                    // 3. Kunci status server secara permanen
                    plugin.getConfig().set("relic_awoken", true);
                    plugin.saveConfig();

                    // 4. Broadcast efek megah
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        // Suara ledakan / naga mati
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0f, 0.5f);
                        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);

                        // Title memenuhi layar
                        Title title = Title.title(
                                Component.text("THE RELIC AWAKENS").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD),
                                Component.text(player.getName() + " has claimed the power!").color(NamedTextColor.GOLD),
                                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(6), Duration.ofSeconds(2))
                        );
                        p.showTitle(title);

                        // Broadcast ngeri di chat
                        p.sendMessage("§8===============================================");
                        p.sendMessage("§4§lPERINGATAN GLOBAL!");
                        p.sendMessage("§c" + player.getName() + " §ftelah membangkitkan §5The Worldheart Relic§f!");
                        p.sendMessage("§fSemua orang... buru dia sebelum terlambat!");
                        p.sendMessage("§8===============================================");
                    }
                } else {
                    player.sendMessage("§e[!] Kamu mencoba membangkitkan Relic, tapi bahanmu kurang!");
                }
            }
        }
    }

    // Fungsi bantuan untuk menghapus persis 1 item bahan
    private void removeMaterial(Player player, org.bukkit.NamespacedKey key) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, org.bukkit.persistence.PersistentDataType.BYTE)) {
                item.setAmount(item.getAmount() - 1);
                break;
            }
        }
    }
}