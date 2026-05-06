package com.relicrequiem.plugin;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OreListener implements Listener {

    private boolean isWorldheartOreBlock(Block block) {
        if (block.getType() == Material.NOTE_BLOCK) {
            NoteBlock noteBlock = (NoteBlock) block.getBlockData();
            return noteBlock.getInstrument() == Instrument.CHIME && noteBlock.getNote().getId() == 10;
        }
        return false;
    }

    // 1. SISTEM HARDNESS
    // Saat player mulai memukul blok
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        if (isWorldheartOreBlock(event.getBlock())) {
            Player player = event.getPlayer();
            // Kasih efek Mining Fatigue Amplifier 1. 
            // Ini bikin durasi nambang naik dari 1 detik jadi sekitar 14 detik (Sangat ideal buat RPG tanpa bikin player AFK bosan).
            // Kalau mau yg bener-bener 80 detik (level bedrock), ganti angka 1 jadi 2.
            player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 20 * 120, 2, false, false, false));
        }
    }

    // Saat player berhenti memukul atau ganti item (batal nambang)
    @EventHandler
    public void onBlockDamageAbort(BlockDamageAbortEvent event) {
        Player player = event.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.MINING_FATIGUE)) {
            player.removePotionEffect(PotionEffectType.MINING_FATIGUE);
        }
    }

    // Saat blok akhirnya hancur
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (isWorldheartOreBlock(block)) {
            // Hapus efek Fatigue-nya
            if (player.hasPotionEffect(PotionEffectType.MINING_FATIGUE)) {
                player.removePotionEffect(PotionEffectType.MINING_FATIGUE);
            }

            event.setDropItems(false);
            ItemStack tool = player.getInventory().getItemInMainHand();

            if (tool.getType() == Material.DIAMOND_PICKAXE || tool.getType() == Material.NETHERITE_PICKAXE) {
                block.getWorld().dropItemNaturally(block.getLocation(), MaterialManager.createWorldheartGem());
            } else {
                player.sendMessage("§c[!] Beliungmu hancur berkeping-keping! Bijih ini butuh Diamond Pickaxe...");
            }
        }
    }

    // 2. SISTEM KEKEBALAN MUTLAK
    // Kebal Ledakan Creeper / TNT
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(this::isWorldheartOreBlock);
    }

    // Kebal Ledakan Bed / Respawn Anchor
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(this::isWorldheartOreBlock);
    }

    // Kebal Dorongan Piston
    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (isWorldheartOreBlock(block)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    // Kebal Tarikan Piston (Sticky Piston)
    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            if (isWorldheartOreBlock(block)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    // 3. SISTEM STABILITAS
    @EventHandler
    public void onNoteClick(PlayerInteractEvent event) {
        if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block != null && isWorldheartOreBlock(block)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (isWorldheartOreBlock(block)) {
            event.setCancelled(true); 
            block.getState().update(true, false); 
        }
    }
}