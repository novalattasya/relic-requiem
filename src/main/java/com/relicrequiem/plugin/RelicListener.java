package com.relicrequiem.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class RelicListener implements Listener {

    private final Map<UUID, Boolean> keepRelicMap = new HashMap<>();

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (RelicManager.isRelic(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c[!] Relic telah terikat dengan jiwamu. Kamu tidak bisa membuangnya!");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        if (event.getClickedInventory() != null && event.getClickedInventory().getType() != InventoryType.PLAYER) {
            if (RelicManager.isRelic(cursor) || (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && RelicManager.isRelic(clicked))) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage("§c[!] Relic hanya bisa disimpan di dalam tasmu!");
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer != null && killer != player) {
            event.getDrops().add(MaterialManager.createFallenSoul());
        }

        Iterator<ItemStack> drops = event.getDrops().iterator();
        while (drops.hasNext()) {
            ItemStack drop = drops.next();
            if (RelicManager.isRelic(drop)) {
                if (killer != null) {
                    Bukkit.broadcastMessage("§4§l[PENGUMUMAN] §c" + player.getName() + " telah dibunuh oleh " + killer.getName() + "! Relic kini jatuh ke tanah!");
                } else {
                    drops.remove();
                    keepRelicMap.put(player.getUniqueId(), true);
                    player.sendMessage("§d[!] Relic melindungimu dari kehampaan. Item ini akan kembali saat kamu respawn.");
                }
                break;
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (keepRelicMap.containsKey(player.getUniqueId())) {
            player.getInventory().addItem(RelicManager.createRelic());
            keepRelicMap.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onItemDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Item itemEntity) {
            if (RelicManager.isRelic(itemEntity.getItemStack())) {
                event.setCancelled(true); 
                
                if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    itemEntity.teleport(itemEntity.getWorld().getHighestBlockAt(itemEntity.getLocation()).getLocation().add(0, 1, 0));
                }
            }
        }
    }

    @EventHandler
    public void onItemCombust(EntityCombustEvent event) {
        if (event.getEntity() instanceof Item itemEntity) {
            if (RelicManager.isRelic(itemEntity.getItemStack())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        if (RelicManager.isRelic(event.getEntity().getItemStack())) {
            event.setCancelled(true);
        }
    }
}