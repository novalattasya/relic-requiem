package com.relicrequiem.plugin.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OreListener implements Listener {

    private final Map<UUID, BukkitRunnable> activeExtractions = new HashMap<>();
    
    private final int MAX_STEPS = 330; 

    private boolean isWorldheartOreBlock(Block block) {
        if (block.getType() == Material.NOTE_BLOCK) {
            NoteBlock noteBlock = (NoteBlock) block.getBlockData();
            return noteBlock.getInstrument() == Instrument.CHIME && noteBlock.getNote().getId() == 10;
        }
        return false;
    }

    @EventHandler
    public void onOrePunch(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();

            if (block != null && isWorldheartOreBlock(block)) {
                event.setCancelled(true); // Stop event Vanilla

                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    
                    if (activeExtractions.containsKey(player.getUniqueId())) {
                        return;
                    }

                    ItemStack tool = player.getInventory().getItemInMainHand();
                    if (tool.getType() != Material.DIAMOND_PICKAXE && tool.getType() != Material.NETHERITE_PICKAXE) {
                        player.sendMessage("§c[!] Beliungmu terlalu lemah untuk memulai ekstraksi! Butuh Diamond Pickaxe.");
                        return;
                    }

                    startExtraction(player, block);
                }
            }
        }
    }

    private void startExtraction(Player player, Block block) {
        Location loc = block.getLocation();
        player.sendMessage("§d[!] Memulai Ekstraksi Worldheart Ore... Tetaplah dalam radius 6 blok!");

        BukkitRunnable task = new BukkitRunnable() {
            int step = 0;

            @Override
            public void run() {
                // 1. Validasi keberadaan player
                if (!player.isOnline() || player.isDead() || player.getLocation().distance(loc) > 6) {
                    player.sendMessage("§c[!] Ekstraksi Gagal! Kamu terlalu jauh dari Ore.");
                    cleanup(player, loc);
                    return;
                }

                // 2. Validasi Blok
                if (!isWorldheartOreBlock(block)) {
                    cleanup(player, loc);
                    return;
                }

                step++;
                int percentage = (int) (((double) step / MAX_STEPS) * 100);

                // 3. UI Progress Bar
                String progressBar = generateProgressBar(percentage);
                player.sendActionBar(Component.text("§5§lEXTRACTION: " + progressBar + " §d" + percentage + "%"));

                // 4. RETAKAN
                float progress = (float) step / MAX_STEPS;
                block.getWorld().getPlayers().forEach(p -> p.sendBlockDamage(loc, progress, player.getEntityId()));

                // 5. PARTIKEL
                if (step % 4 == 0) {
                    block.getWorld().spawnParticle(Particle.ENCHANT, loc.clone().add(0.5, 0.5, 0.5), 10, 0.4, 0.4, 0.4, 0.1);
                    float pitch = 1.0f + (percentage / 100f);
                    block.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 2.0f, pitch);
                }

                // 6. FINISH
                if (step >= MAX_STEPS) {
                    finishExtraction(player, block);
                    cleanup(player, loc);
                }
            }

            private void cleanup(Player p, Location l) {
                this.cancel();
                activeExtractions.remove(p.getUniqueId());
                // Hapus retakan (HANYA MENGHAPUS EFEK RETAK, BLOK TETAP AMAN)
                l.getWorld().getPlayers().forEach(pl -> pl.sendBlockDamage(l, 0.0f, p.getEntityId()));
            }
        };

        activeExtractions.put(player.getUniqueId(), task);
        task.runTaskTimer(JavaPlugin.getPlugin(RelicRequiemPlugin.class), 0L, 5L);
    }

    private void finishExtraction(Player player, Block block) {
        Location loc = block.getLocation().add(0.5, 0.5, 0.5);

        // HANCURKAN NOTE BLOCK
        block.setType(Material.AIR);

        // =========================================================================
        // PEMBERSIHAN SIHIR ARMOR STAND (RADAR DIBESARKAN JADI 4.0!)
        // =========================================================================
        // Radius Y kita set 4.0 biar pasti nangkap ArmorStand yang terpendam di -2.27
        for (Entity entity : block.getWorld().getNearbyEntities(loc, 1.5, 4.0, 1.5)) {
            if (entity instanceof ArmorStand) {
                if (((ArmorStand) entity).isMarker()) {
                    entity.remove();
                }
            }
        }
        // =========================================================================

        // EFEK LEDAKAN AMAN
        block.getWorld().spawnParticle(Particle.BLOCK, loc, 150, 0.3, 0.3, 0.3, 0.1, Bukkit.createBlockData(Material.AMETHYST_BLOCK));
        block.getWorld().spawnParticle(Particle.END_ROD, loc, 50, 0.4, 0.4, 0.4, 0.05); // Kilauan cahaya epik
        block.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 2.0f, 0.8f);
        block.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.5f);

        // DROP ITEM
        block.getWorld().dropItemNaturally(block.getLocation(), MaterialManager.createWorldheartGem());
        player.sendMessage("§a[!] Ekstraksi Berhasil! Inti bumi telah terlepas.");
    }

    private String generateProgressBar(int percentage) {
        int bars = percentage / 10;
        StringBuilder builder = new StringBuilder("§d[");
        for (int i = 0; i < 10; i++) {
            if (i < bars) builder.append("§5|");
            else builder.append("§7|");
        }
        builder.append("§d]");
        return builder.toString();
    }

    // 2. SISTEM KEKEBALAN MUTLAK 
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (isWorldheartOreBlock(event.getBlock())) {
            event.setCancelled(true);
            if (event.getPlayer().getGameMode() == org.bukkit.GameMode.CREATIVE) {
                finishExtraction(event.getPlayer(), event.getBlock()); 
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(this::isWorldheartOreBlock);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(this::isWorldheartOreBlock);
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (isWorldheartOreBlock(block)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            if (isWorldheartOreBlock(block)) {
                event.setCancelled(true);
                return;
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