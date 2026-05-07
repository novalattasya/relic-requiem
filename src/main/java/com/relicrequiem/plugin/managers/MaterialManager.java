package com.relicrequiem.plugin.managers;

import com.relicrequiem.plugin.config.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Instrument;
import org.bukkit.Note;

import com.relicrequiem.plugin.RelicRequiemPlugin;
import java.util.List;

public class MaterialManager {

    private static final RelicRequiemPlugin plugin = JavaPlugin.getPlugin(RelicRequiemPlugin.class);

    public static final NamespacedKey FALLEN_SOUL_KEY = new NamespacedKey(plugin, "fallen_soul");
    public static final NamespacedKey WORLDHEART_GEM_KEY = new NamespacedKey(plugin, "worldheart_gem");
    public static final NamespacedKey DOMINION_CORE_KEY = new NamespacedKey(plugin, "dominion_core");
    public static final NamespacedKey WORLDHEART_ORE_KEY = new NamespacedKey(plugin, "worldheart_ore");

    public static ItemStack createFallenSoul() {
        ConfigManager config = ConfigManager.getInstance();
        ItemStack item = new ItemStack(Material.PHANTOM_MEMBRANE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(config.getFallenSoulName()).color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            
            List<String> lore = config.getFallenSoulLore();
            if (!lore.isEmpty()) {
                meta.lore(lore.stream()
                        .map(line -> Component.text(line).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
                        .toList());
            }
            
            meta.setCustomModelData(2);
            meta.getPersistentDataContainer().set(FALLEN_SOUL_KEY, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static boolean isFallenSoul(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(FALLEN_SOUL_KEY, PersistentDataType.BYTE);
    }

    public static ItemStack createWorldheartGem() {
        ConfigManager config = ConfigManager.getInstance();
        ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(config.getWorldheartGemName()).color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            
            List<String> lore = config.getWorldheartGemLore();
            if (!lore.isEmpty()) {
                meta.lore(lore.stream()
                        .map(line -> Component.text(line).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
                        .toList());
            }
            
            meta.setCustomModelData(3);
            meta.getPersistentDataContainer().set(WORLDHEART_GEM_KEY, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static boolean isWorldheartGem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(WORLDHEART_GEM_KEY, PersistentDataType.BYTE);
    }

    public static ItemStack createDominionCore() {
        ConfigManager config = ConfigManager.getInstance();
        ItemStack item = new ItemStack(Material.ECHO_SHARD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(config.getDominionCoreName()).color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            
            List<String> lore = config.getDominionCoreLore();
            if (!lore.isEmpty()) {
                meta.lore(lore.stream()
                        .map(line -> Component.text(line).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
                        .toList());
            }
            
            meta.setCustomModelData(4);
            meta.getPersistentDataContainer().set(DOMINION_CORE_KEY, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static boolean isDominionCore(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(DOMINION_CORE_KEY, PersistentDataType.BYTE);
    }

    public static ItemStack createWorldheartOre() {
        ConfigManager config = ConfigManager.getInstance();
        ItemStack item = new ItemStack(Material.NOTE_BLOCK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(config.getWorldheartOreName()).color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.ITALIC, false));
            
            List<String> lore = config.getWorldheartOreLore();
            if (!lore.isEmpty()) {
                meta.lore(lore.stream()
                        .map(line -> Component.text(line).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
                        .toList());
            }
            
            meta.setCustomModelData(5);
            meta.getPersistentDataContainer().set(WORLDHEART_ORE_KEY, PersistentDataType.BYTE, (byte) 1);

            if (meta instanceof BlockDataMeta blockDataMeta) {
                BlockData data = org.bukkit.Bukkit.createBlockData(Material.NOTE_BLOCK);
                NoteBlock noteBlock = (NoteBlock) data;
                noteBlock.setInstrument(Instrument.CHIME);
                noteBlock.setNote(new Note(10));
                blockDataMeta.setBlockData(noteBlock);
            }

            item.setItemMeta(meta);
        }
        return item;
    }

    public static boolean isWorldheartOreItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(WORLDHEART_ORE_KEY, PersistentDataType.BYTE);
    }
}