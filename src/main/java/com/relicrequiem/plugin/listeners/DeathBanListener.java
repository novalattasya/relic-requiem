package com.relicrequiem.plugin.listeners;

import com.relicrequiem.plugin.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class DeathBanListener implements Listener {

    private final ConfigManager config = ConfigManager.getInstance();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // Only ban if killed by another player (PK)
        if (killer != null && killer instanceof Player) {
            int banDurationHours = config.getDeathBanDurationHours();
            if (banDurationHours > 0) {
                long banTimestamp = System.currentTimeMillis() + (banDurationHours * 60 * 60 * 1000L);
                config.setBannedPlayer(victim.getUniqueId().toString(), banTimestamp);

                // Kick the victim immediately
                String kickMessage = config.getDeathBanKickMessage().replace("%hours%", String.valueOf(banDurationHours));
                victim.kickPlayer(kickMessage);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        if (config.isPlayerBanned(uuid)) {
            long banTimestamp = config.getPlayerBanTimestamp(uuid);
            long currentTime = System.currentTimeMillis();

            if (currentTime < banTimestamp) {
                // Still banned
                long remainingMillis = banTimestamp - currentTime;
                long remainingHours = remainingMillis / (60 * 60 * 1000L);
                long remainingMinutes = (remainingMillis % (60 * 60 * 1000L)) / (60 * 1000L);

                String kickMessage = config.getDeathBanJoinKickMessage()
                    .replace("%hours%", String.valueOf(remainingHours))
                    .replace("%minutes%", String.valueOf(remainingMinutes));
                player.kickPlayer(kickMessage);
            } else {
                // Ban expired, remove from config
                config.removeBannedPlayer(uuid);
            }
        }
    }
}