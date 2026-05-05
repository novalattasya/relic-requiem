package com.relicrequiem.plugin;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class OreListener implements Listener {

    // Fungsi cek
    private boolean isWorldheartOreBlock(Block block) {
        if (block.getType() == Material.NOTE_BLOCK) {
            NoteBlock noteBlock = (NoteBlock) block.getBlockData();
            // Cek sidik fingerprint: Instrumen Chime & Nada 10
            return noteBlock.getInstrument() == Instrument.CHIME && noteBlock.getNote().getId() == 10;
        }
        return false;
    }

    // EVENT 1: Admin menaruh balok Worldheart Ore
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ItemStack itemInHand = event.getItemInHand();
        
        if (MaterialManager.isWorldheartOreItem(itemInHand)) {
            Block block = event.getBlockPlaced();
            NoteBlock noteBlock = (NoteBlock) block.getBlockData();
            
            // Suntikkan "sidik jari" rahasia
            noteBlock.setInstrument(Instrument.CHIME);
            noteBlock.setNote(new Note(10));
            block.setBlockData(noteBlock);
            
            event.getPlayer().sendMessage("§a[!] Worldheart Ore berhasil ditempatkan!");
        }
    }

    // EVENT 2: Player menambang balok
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (isWorldheartOreBlock(block)) {
            // 1. Apapun yang terjadi, JANGAN drop Note Block aslinya
            event.setDropItems(false);

            Player player = event.getPlayer();
            ItemStack tool = player.getInventory().getItemInMainHand();

            // 2. Cek apakah pakai Diamond Pickaxe atau Netherite Pickaxe
            if (tool.getType() == Material.DIAMOND_PICKAXE || tool.getType() == Material.NETHERITE_PICKAXE) {
                // Berhasil! Drop Worldheart Gem di lokasi balok hancur
                block.getWorld().dropItemNaturally(block.getLocation(), MaterialManager.createWorldheartGem());
                
                // (Achievement "menemukan rahasia inti bumi" akan otomatis terpicu dari AchievementListener saat dipungut!)
            } else {
                // Gagal! Cuma dapet pesan nyesek
                player.sendMessage("§c[!] Beliungmu hancur berkeping-keping! Bijih ini butuh Diamond Pickaxe...");
            }
        }
    }
}