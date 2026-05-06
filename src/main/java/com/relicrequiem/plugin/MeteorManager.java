package com.relicrequiem.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

public class MeteorManager {

    // 1. SPAWN NATURAL (Setiap 3 Jam, Acak di Radius 4000x4000)
    public static void spawnRandomMeteor() {
        World world = Bukkit.getWorlds().get(0);
        int x = 0, z = 0;
        Block targetGround = null;
        boolean validLocation = false;

        // Looping: Terus acak kordinat sampai nemu TANAH ALAMI (bukan air, bukan rumah)
        while (!validLocation) {
            x = ThreadLocalRandom.current().nextInt(-2000, 2001);
            z = ThreadLocalRandom.current().nextInt(-2000, 2001);
            
            // Cari blok tertinggi yang BUKAN udara
            Block highest = world.getHighestBlockAt(x, z);
            
            // Cek apakah itu tanah lapang alami
            if (isNaturalGround(highest.getType())) {
                targetGround = highest;
                validLocation = true;
            }
        }

        // Eksekusi kawah dan meteor
        executeMeteorSpawn(world, x, targetGround.getY() - 1, z, true);
    }

    // 2. SPAWN DEBUG ADMIN
    public static void spawnMeteorNearPlayer(Player admin) {
        Location adminLoc = admin.getLocation();
        World world = admin.getWorld();
        
        int distance = ThreadLocalRandom.current().nextInt(5, 11);
        Location targetLoc = adminLoc.clone().add(adminLoc.getDirection().setY(0).normalize().multiply(distance));
        
        int x = targetLoc.getBlockX();
        int z = targetLoc.getBlockZ();
        
        Block highestBlock = world.getHighestBlockAt(x, z);
        
        // Turun ke bawah terus sampai nemu blok padat (mengabaikan obor, rumput tinggi, air)
        while (highestBlock.getY() > -60 && (!highestBlock.getType().isSolid() || 
               highestBlock.getType() == Material.WATER || highestBlock.getType() == Material.LAVA)) {
            highestBlock = highestBlock.getRelative(0, -1, 0);
        }

        executeMeteorSpawn(world, x, highestBlock.getY() - 1, z, false);
    }

    // FUNGSI BANTUAN: Deteksi Tanah Alami (Anti-Rumah & Anti-Air)
    private static boolean isNaturalGround(Material type) {
        return type == Material.GRASS_BLOCK || type == Material.DIRT || type == Material.COARSE_DIRT ||
               type == Material.PODZOL || type == Material.STONE || type == Material.SAND ||
               type == Material.RED_SAND || type == Material.TERRACOTTA || type == Material.SNOW_BLOCK ||
               type == Material.GRAVEL || type.name().endsWith("_TERRACOTTA");
    }

    // 3. MESIN EKSEKUSI KAWAH & METEOR
    private static void executeMeteorSpawn(World world, int x, int y, int z, boolean isGlobalBroadcast) {
        
        // 1. GENERATE KAWAH (Radius 3 Block)
        int radius = 3;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    double distanceSq = dx * dx + dy * dy + dz * dz;
                    
                    if (distanceSq <= radius * radius) {
                        Block b = world.getBlockAt(x + dx, y + dy, z + dz);
                        
                        if (b.getType() == Material.BEDROCK) continue; // Jangan hancurin bedrock
                        if (dx == 0 && dy == 0 && dz == 0) continue; // Jangan sentuh titik tengah (tempat Ore)

                        if (distanceSq < (radius - 1) * (radius - 1)) {
                            // Inti kawah: Menguap jadi udara
                            b.setType(Material.AIR);
                        } else {
                            // Pinggiran kawah: Tanah hangus (Magma & Batu)
                            if (b.getType().isSolid() && !b.getType().isAir()) {
                                if (ThreadLocalRandom.current().nextBoolean()) {
                                    b.setType(Material.MAGMA_BLOCK);
                                } else {
                                    b.setType(Material.COBBLESTONE);
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. SULAP NOTE BLOCK
        Block targetBlock = world.getBlockAt(x, y, z);
        org.bukkit.block.data.BlockData data = Bukkit.createBlockData(Material.NOTE_BLOCK);
        NoteBlock noteBlock = (NoteBlock) data;
        noteBlock.setInstrument(Instrument.CHIME);
        noteBlock.setNote(new Note(10));
        targetBlock.setBlockData(noteBlock, false); // false = jangan update blok sekitarnya biar gak kedip

        // =========================================================================
        // 2.5 TRIK SIHIR ARMOR STAND (VERSI ANGKA SAKTI HASIL EKSPERIMEN)
        // =========================================================================
        // Posisi Y diturunin -2.27 sesuai hitungan presisi Bedrock
        Location asLoc = targetBlock.getLocation().add(0.5, -2.27, 0.5); 
        ArmorStand armorStand = world.spawn(asLoc, ArmorStand.class);
        
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        
        // Skala 1.62 biar ukurannya pas 1 Blok
        if (armorStand.getAttribute(Attribute.GENERIC_SCALE) != null) {
            armorStand.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.62);
        }
        
        // Pakaikan item Worldheart Ore custom lu sebagai Helm!
        armorStand.getEquipment().setHelmet(MaterialManager.createWorldheartOre());
        // =========================================================================

        // 3. EFEK PETIR EPIK
        world.strikeLightningEffect(targetBlock.getLocation());
        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(RelicRequiemPlugin.class), () -> {
            world.strikeLightningEffect(targetBlock.getLocation().add(1, 0, 1));
            world.strikeLightningEffect(targetBlock.getLocation().add(-1, 0, -1));
        }, 10L);

        // 4. PENGUMUMAN
        if (isGlobalBroadcast) {
            Bukkit.broadcastMessage("§8===============================================");
            Bukkit.broadcastMessage("§6§l☄ METEOR JATUH!");
            Bukkit.broadcastMessage("§cSebuah §5Worldheart Ore §ctelah menghantam bumi!");
            Bukkit.broadcastMessage("§eLokasi Terdeteksi: §fX: " + x + " §7| §fZ: " + z);
            Bukkit.broadcastMessage("§cRebut inti bumi itu sebelum musuhmu menemukannya!");
            Bukkit.broadcastMessage("§8===============================================");
        } else {
            Bukkit.broadcastMessage("§8[ADMIN DEBUG] §6☄ Kawah Meteor berhasil diciptakan di X: " + x + " Z: " + z);
        }
    }
}