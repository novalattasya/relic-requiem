package com.relicrequiem.plugin.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private static ConfigManager instance;
    private final JavaPlugin plugin;
    private Configuration config;

    private ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.reload();
    }

    public static void initialize(JavaPlugin plugin) {
        if (instance == null) {
            instance = new ConfigManager(plugin);
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ConfigManager not initialized!");
        }
        return instance;
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    // ============================================
    // WORLD SETTINGS
    // ============================================
    public double getWorldBorderSize() {
        return config.getDouble("world.border-size", 8000.0);
    }

    // ============================================
    // METEOR SYSTEM
    // ============================================
    public int getMeteorSpawnIntervalHours() {
        return config.getInt("meteor.spawn-interval-hours", 3);
    }

    public int getMeteorSpawnRadius() {
        return config.getInt("meteor.spawn-radius", 2000);
    }

    public int getMeteorCraterRadius() {
        return config.getInt("meteor.crater-radius", 3);
    }

    public double getArmorStandYOffset() {
        return config.getDouble("meteor.armor-stand-y-offset", -2.27);
    }

    public double getArmorStandScale() {
        return config.getDouble("meteor.armor-stand-scale", 1.62);
    }

    // ============================================
    // ORE EXTRACTION SYSTEM
    // ============================================
    public int getOreExtractionDuration() {
        return config.getInt("ore-extraction.duration-ticks", 330);
    }

    public double getOreDestructionSearchRadius() {
        return config.getDouble("ore-extraction.destruction-search-radius", 4.0);
    }

    // ============================================
    // DROP CHANCES
    // ============================================
    public double getDropChance(String mobName) {
        return config.getDouble("drop-chances." + mobName.toLowerCase(), 0.0);
    }

    // ============================================
    // CUSTOM ITEMS - NAMES & LORE
    // ============================================
    public String getFallenSoulName() {
        return config.getString("items.fallen-soul.name", "Fallen Soul");
    }

    public String getWorldheartGemName() {
        return config.getString("items.worldheart-gem.name", "Worldheart Gem");
    }

    public String getDominionCoreName() {
        return config.getString("items.dominion-core.name", "Dominion Core");
    }

    public String getWorldheartOreName() {
        return config.getString("items.worldheart-ore.name", "Worldheart Ore");
    }

    public String getRelicName() {
        return config.getString("items.relic.name", "The Worldheart Relic");
    }

    public java.util.List<String> getFallenSoulLore() {
        return config.getStringList("items.fallen-soul.lore");
    }

    public java.util.List<String> getWorldheartGemLore() {
        return config.getStringList("items.worldheart-gem.lore");
    }

    public java.util.List<String> getDominionCoreLore() {
        return config.getStringList("items.dominion-core.lore");
    }

    public java.util.List<String> getWorldheartOreLore() {
        return config.getStringList("items.worldheart-ore.lore");
    }

    public java.util.List<String> getRelicLore() {
        return config.getStringList("items.relic.lore");
    }

    // ============================================
    // MESSAGES - RELIC PROTECTION
    // ============================================
    public String getRelicDropBlocked() {
        return config.getString("messages.relic-protection.drop-blocked", 
            "§c[!] Relic is bound to your soul. You cannot drop it!");
    }

    public String getRelicStorageBlocked() {
        return config.getString("messages.relic-protection.storage-blocked", 
            "§c[!] Relic can only be stored in your inventory!");
    }

    public String getInsufficientPermissions() {
        return config.getString("messages.relic-protection.insufficient-permissions", 
            "§cInsufficient permissions.");
    }

    // ============================================
    // MESSAGES - RELIC CRAFTING
    // ============================================
    public String getRelicCraftingSuccess() {
        return config.getString("messages.relic-crafting.success", 
            "§aRelic item generated.");
    }

    public String getMaterialsGenerated() {
        return config.getString("messages.relic-crafting.materials-generated", 
            "§aAwakening materials generated.");
    }

    public String getRelicAlreadyAwoken() {
        return config.getString("messages.relic-crafting.already-awoken", 
            "§c[!] The Worldheart Relic has been awakened by someone. This opportunity is gone!");
    }

    public String getMissingMaterials() {
        return config.getString("messages.relic-crafting.missing-materials", 
            "§c[!] You need all three materials: Fallen Soul, Worldheart Gem, and Dominion Core!");
    }

    public String getAwakeningBroadcast() {
        return config.getString("messages.relic-crafting.awakening-broadcast", 
            "§4§l[ANNOUNCEMENT] §c%player% has awakened The Worldheart Relic!");
    }

    public String getAwakeningPersonal() {
        return config.getString("messages.relic-crafting.awakening-personal", 
            "§d[!] The power of the Relic flows through you. Your location is now exposed to the entire server!");
    }

    // ============================================
    // MESSAGES - DEATH
    // ============================================
    public String getRelicDropped() {
        return config.getString("messages.death.relic-dropped", 
            "§4§l[ANNOUNCEMENT] §c%player% has been killed by %killer%! The Relic now falls to the ground!");
    }

    public String getRelicProtected() {
        return config.getString("messages.death.relic-protected", 
            "§d[!] The Relic protects you from the void. This item will return when you respawn.");
    }

    // ============================================
    // MESSAGES - ORE EXTRACTION
    // ============================================
    public String getOreExtractionStarted() {
        return config.getString("messages.ore-extraction.started", 
            "§6[⛏] Starting extraction... This will take time.");
    }

    public String getOreExtractionCancelled() {
        return config.getString("messages.ore-extraction.cancelled", 
            "§c[✗] You moved too far! Extraction cancelled.");
    }

    public String getOreExtractionCompleted() {
        return config.getString("messages.ore-extraction.completed", 
            "§a[✓] Extraction complete! You obtained Worldheart Gem.");
    }

    public String getOreExtractionWrongTool() {
        return config.getString("messages.ore-extraction.wrong-tool", 
            "§c[!] You need a Diamond or Netherite pickaxe to extract this ore!");
    }

    // ============================================
    // MESSAGES - ACHIEVEMENTS
    // ============================================
    public String getAchievementFallenSoul() {
        return config.getString("messages.achievements.fallen-soul", 
            "§d§l[ACHIEVEMENT] §e%player% has discovered a fragment of the soul... §8[Fallen Soul]");
    }

    public String getAchievementWorldheartGem() {
        return config.getString("messages.achievements.worldheart-gem", 
            "§d§l[ACHIEVEMENT] §e%player% has excavated the secret of the world's core... §8[Worldheart Gem]");
    }

    public String getAchievementDominionCore() {
        return config.getString("messages.achievements.dominion-core", 
            "§d§l[ACHIEVEMENT] §e%player% has seized the power of dominion! §8[Dominion Core]");
    }

    // ============================================
    // MESSAGES - RELIC EFFECTS
    // ============================================
    public String getRelicEffectMarked() {
        return config.getString("messages.relic-effects.marked", 
            "§4§l[!] RELIC AURA ERUPTS! Your position is now exposed as a RED MARKER on the Locator Bar!");
    }

    // ============================================
    // MESSAGES - ADMIN
    // ============================================
    public String getAdminReloadStart() {
        return config.getString("messages.admin.reload-start", 
            "§eReloading Relic Requiem config...");
    }

    public String getAdminReloadSuccess() {
        return config.getString("messages.admin.reload-success", 
            "§a[✓] Config reloaded successfully!");
    }

    public String getAdminReloadFailed() {
        return config.getString("messages.admin.reload-failed", 
            "§c[✗] Failed to reload config. Check console for errors.");
    }

    public String getAdminMeteorSpawned() {
        return config.getString("messages.admin.meteor-spawned", 
            "§aA meteor has been spawned near you!");
    }

    public String getAdminRelicReset() {
        return config.getString("messages.admin.relic-reset", 
            "§a[!] Server relic status has been reset.");
    }

    // ============================================
    // MESSAGES - DEATH BAN
    // ============================================
    public String getDeathBanKickMessage() {
        return config.getString("messages.death-ban.kick", 
            "§cYou have been temporarily banned for %hours% hours due to being killed by another player.");
    }

    public String getDeathBanJoinKickMessage() {
        return config.getString("messages.death-ban.join-kick", 
            "§cYou are temporarily banned for being killed by another player.\n§cTime remaining: %hours% hours and %minutes% minutes.");
    }

    // ============================================
    // CONFIG KEYS
    // ============================================
    public boolean isRelicAwakened() {
        return config.getBoolean("relic-awakened", false);
    }

    public void setRelicAwakened(boolean awakened) {
        config.set("relic-awakened", awakened);
        plugin.saveConfig();
    }

    // ============================================
    // DEATH BAN SYSTEM
    // ============================================
    public int getDeathBanDurationHours() {
        return config.getInt("death-ban.duration-hours", 3);
    }

    // ============================================
    // BANNED PLAYERS
    // ============================================
    public java.util.Map<String, Long> getBannedPlayers() {
        org.bukkit.configuration.ConfigurationSection section = config.getConfigurationSection("banned-players");
        if (section == null) return new java.util.HashMap<>();
        return section.getValues(false).entrySet().stream()
            .collect(java.util.stream.Collectors.toMap(
                e -> e.getKey(),
                e -> ((Number) e.getValue()).longValue()
            ));
    }

    public void setBannedPlayer(String uuid, long banTimestamp) {
        config.set("banned-players." + uuid, banTimestamp);
        plugin.saveConfig();
    }

    public void removeBannedPlayer(String uuid) {
        config.set("banned-players." + uuid, null);
        plugin.saveConfig();
    }

    public boolean isPlayerBanned(String uuid) {
        return config.contains("banned-players." + uuid);
    }

    public long getPlayerBanTimestamp(String uuid) {
        return config.getLong("banned-players." + uuid, 0L);
    }
}
