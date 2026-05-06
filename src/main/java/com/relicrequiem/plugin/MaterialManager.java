package com.relicrequiem.plugin;

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


import java.util.List;

public class MaterialManager {

    private static final RelicRequiemPlugin plugin = JavaPlugin.getPlugin(RelicRequiemPlugin.class);

    public static final NamespacedKey FALLEN_SOUL_KEY = new NamespacedKey(plugin, "fallen_soul");
    public static final NamespacedKey WORLDHEART_GEM_KEY = new NamespacedKey(plugin, "worldheart_gem");
    public static final NamespacedKey DOMINION_CORE_KEY = new NamespacedKey(plugin, "dominion_core");
    public static final NamespacedKey WORLDHEART_ORE_KEY = new NamespacedKey(plugin, "worldheart_ore");

    // 1. FALLEN SOUL
    public static ItemStack createFallenSoul() {
        ItemStack item = new ItemStack(Material.PHANTOM_MEMBRANE); // Aman dari achievement
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Fallen Soul").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Setiap jiwa yang jatuh tidak hilang begitu saja,").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("mereka menjadi bahan bakar perang berikutnya.").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            ));
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

    // 2. WORLDHEART GEM
    public static ItemStack createWorldheartGem() {
        ItemStack item = new ItemStack(Material.AMETHYST_SHARD); // Aman dari achievement
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Worldheart Gem").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Jantung dunia yang mengeras oleh ribuan perang,").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("hanya yang bertahan yang bisa menemukannya.").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            ));
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

    // 3. DOMINION CORE
    public static ItemStack createDominionCore() {
        ItemStack item = new ItemStack(Material.ECHO_SHARD); // Aman dari achievement
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Dominion Core").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Kekuatan untuk memerintah bukan diwariskan,").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("tapi direbut dari kekacauan.").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            ));
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

    // 4. WORLDHEART ORE (Block Item)
    public static ItemStack createWorldheartOre() {
        ItemStack item = new ItemStack(Material.NOTE_BLOCK); 
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Worldheart Ore").color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Batu kuno yang menyimpan inti dunia.").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("Hanya beliung terkuat yang bisa menambangnya!").color(NamedTextColor.DARK_RED).decoration(TextDecoration.ITALIC, false)
            ));
            meta.setCustomModelData(5); 
            meta.getPersistentDataContainer().set(WORLDHEART_ORE_KEY, PersistentDataType.BYTE, (byte) 1);

            // INI SIHIRNYA: Suntikkan DNA Blok langsung ke dalam Item!
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