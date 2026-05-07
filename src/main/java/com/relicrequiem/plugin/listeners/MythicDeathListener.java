package com.relicrequiem.plugin.listeners;

import com.relicrequiem.plugin.config.ConfigManager;
import com.relicrequiem.plugin.managers.MaterialManager;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.ThreadLocalRandom;

public class MythicDeathListener implements Listener {

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event) {
        ConfigManager config = ConfigManager.getInstance();
        String internalName = event.getMobType().getInternalName().toLowerCase();
        double dropChance = config.getDropChance(internalName);

        if (dropChance > 0 && ThreadLocalRandom.current().nextDouble() <= dropChance) {
            event.getDrops().add(MaterialManager.createDominionCore());
        }
    }
}