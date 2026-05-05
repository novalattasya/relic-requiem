package com.relicrequiem.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class RelicManager {

    public static final NamespacedKey RELIC_KEY = new NamespacedKey(JavaPlugin.getPlugin(RelicRequiemPlugin.class), "is_relic");

    // FUNGSI 1: MENCIPTAKAN RELIC
    public static ItemStack createRelic() {
        ItemStack relic = new ItemStack(Material.PAPER);
        ItemMeta meta = relic.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text("The Worldheart Relic")
                    .color(NamedTextColor.DARK_PURPLE)
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false));
            
            meta.lore(List.of(
                    Component.text("Artefak penentu nasib dunia.").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("Hanya satu yang boleh berkuasa!").color(NamedTextColor.DARK_RED).decoration(TextDecoration.ITALIC, false)
            ));

            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setCustomModelData(1);
            meta.getPersistentDataContainer().set(RELIC_KEY, PersistentDataType.BYTE, (byte) 1);

            relic.setItemMeta(meta);
        }
        return relic;
    }

    // MENGECEK APAKAH ITEM ADALAH RELIC
    public static boolean isRelic(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(RELIC_KEY, PersistentDataType.BYTE);
    }
}