package com.relicrequiem.plugin.listeners;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MythicDeathListener implements Listener {

    private static final Map<String, Double> DROP_CHANCES = new HashMap<>();

    static {
        // Tier 1: Bosses
        DROP_CHANCES.put("skeletonking", 0.80);

        // Tier 2: Mini-Bosses / High Health
        DROP_CHANCES.put("angrysludge", 0.40);
        DROP_CHANCES.put("staticallychargedsheep", 0.20);

        // Tier 3: Elites
        DROP_CHANCES.put("skeletalknight", 0.15);

        // Tier 4: Minions / Common
        DROP_CHANCES.put("skeletalminion", 0.005);
    }

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event) {
        String internalName = event.getMobType().getInternalName().toLowerCase();
        Double dropChance = DROP_CHANCES.get(internalName);

        if (dropChance != null) {
            if (ThreadLocalRandom.current().nextDouble() <= dropChance) {
                event.getDrops().add(MaterialManager.createDominionCore());
            }
        }
    }
}