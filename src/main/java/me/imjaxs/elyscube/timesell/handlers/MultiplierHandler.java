package me.imjaxs.elyscube.timesell.handlers;

import com.google.common.collect.Maps;
import me.imjaxs.elyscube.timesell.TimeSell;
import me.imjaxs.elyscube.timesell.objects.multiplier.Multiplier;
import me.imjaxs.elyscube.timesell.objects.multiplier.PermissionMultiplier;
import me.imjaxs.elyscube.timesell.objects.multiplier.TemporaryMultiplier;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class MultiplierHandler {
    private final TimeSell plugin = TimeSell.getInstance();
    private final Map<UUID, Multiplier> multipliers = Maps.newHashMap();
    private final Map<UUID, TemporaryMultiplier> temporaryMultipliers = Maps.newHashMap();
    private final Map<Integer, PermissionMultiplier> permissionMultipliers = Maps.newHashMap();

    public Multiplier getMultiplier(Player player) {
        return this.multipliers.get(player.getUniqueId());
    }

    public TemporaryMultiplier getTempMultiplier(Player player) {
        return this.temporaryMultipliers.get(player.getUniqueId());
    }

    public PermissionMultiplier getPermMultiplier(Player player) {
        for (int identifier : this.permissionMultipliers.keySet()) {
            PermissionMultiplier multiplier = this.permissionMultipliers.get(identifier);

            if (player.hasPermission(multiplier.getPermission()))
                return multiplier;
        }
        return null;
    }

    public double getAllMultipliers(Player player) {
        double multiplier = 1.0;

        Multiplier var = this.getMultiplier(player);
        if (var != null)
            multiplier += var.getMultiplier();

        TemporaryMultiplier var1 = this.getTempMultiplier(player);
        if (var1 != null)
            multiplier += var1.getMultiplier();

        PermissionMultiplier var2 = this.getPermMultiplier(player);
        if (var2 != null)
            multiplier += var2.getMultiplier();

        return multiplier;
    }

    public void loadFromFile() {
        this.loadSavedMultipliers();
        this.loadSavedTempMultipliers();
        this.loadPermMultipliers();
    }

    private void loadSavedMultipliers() {
        FileConfiguration configuration = this.plugin.getFileMultipliers().getFileConfiguration();
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            ConfigurationSection sections = configuration.getConfigurationSection("multipliers");
            if (sections == null) return;

            sections.getKeys(false).forEach(multiplierUUID -> {
                try {
                    ConfigurationSection section = sections.getConfigurationSection(multiplierUUID);

                    Multiplier multiplier = new Multiplier(UUID.fromString(section.getString("player")), section.getDouble("multiplier"));
                    this.multipliers.put(multiplier.getUniqueID(), multiplier);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
    }

    private void loadSavedTempMultipliers() {
        FileConfiguration configuration = this.plugin.getFileTempMultipliers().getFileConfiguration();
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            ConfigurationSection sections = configuration.getConfigurationSection("temporary-multipliers");
            if (sections == null) return;

            sections.getKeys(false).forEach(multiplierUUID -> {
                try {
                    ConfigurationSection section = sections.getConfigurationSection(multiplierUUID);

                    TemporaryMultiplier multiplier = new TemporaryMultiplier(UUID.fromString(section.getString("player")), section.getDouble("multiplier"), section.getInt("seconds"));
                    this.temporaryMultipliers.put(multiplier.getUniqueID(), multiplier);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
    }

    private void loadPermMultipliers() {
        FileConfiguration configuration = this.plugin.getFilePermMultipliers().getFileConfiguration();
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            ConfigurationSection sections = configuration.getConfigurationSection("permission-multipliers");

            sections.getKeys(false).forEach(name -> {
                try {
                    ConfigurationSection section = sections.getConfigurationSection(name);

                    PermissionMultiplier multiplier = new PermissionMultiplier(name, section.getString("permission"), section.getInt("priority"), section.getDouble("multiplier"));
                    this.permissionMultipliers.put(multiplier.getPriority(), multiplier);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
    }
}
