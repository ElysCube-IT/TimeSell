package me.imjaxs.elyscube.timesell;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class TimeSell extends JavaPlugin {
    @Getter private static TimeSell instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }
}
