package me.imjaxs.elyscube.timesell;

import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import lombok.Getter;
import me.imjaxs.elyscube.timesell.commands.CommandManager;
import me.imjaxs.elyscube.timesell.handlers.MultiplierHandler;
import me.imjaxs.elyscube.timesell.handlers.ShopHandler;
import me.imjaxs.elyscube.timesell.listeners.BlockListener;
import me.imjaxs.elyscube.timesell.tools.file.FileResource;
import me.jet315.prisonmines.JetsPrisonMines;
import me.jet315.prisonmines.JetsPrisonMinesAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class TimeSell extends JavaPlugin {
    @Getter private static TimeSell instance;

    @Getter private JetsPrisonMinesAPI minesAPI;
    @Getter private Economy vaultAPI;

    @Getter private FileResource fileMessages;
    @Getter private FileResource fileMultipliers;
    @Getter private FileResource fileTempMultipliers;
    @Getter private FileResource filePermMultipliers;

    @Getter private MultiplierHandler multipliers;
    @Getter private ShopHandler shops;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        if (!setupMessages() || !setupMultipliers() || !setupTempMultipliers() || !setupPermMultipliers() || !setupMines() || !this.setupEconomy() || !this.setupTokens())
            this.getServer().getPluginManager().disablePlugin(this);

        this.multipliers = new MultiplierHandler();
        this.multipliers.loadFromFile();

        this.shops = new ShopHandler();
        this.shops.loadFromFile();

        this.getCommand("timesell").setExecutor(new CommandManager());

        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
    }

    @Override
    public void onDisable() {

    }

    private boolean setupMessages() {
        this.fileMessages = new FileResource(this.getDataFolder(), "messages.yml");
        if (!this.fileMessages.exists())
            this.fileMessages.create();

        this.fileMessages.load();
        return this.fileMessages.exists();
    }

    private boolean setupMultipliers() {
        this.fileMultipliers = new FileResource(this.getDataFolder(), "multipliers.yml");
        if (!this.fileMultipliers.exists())
            this.fileMultipliers.create();

        this.fileMultipliers.load();
        return this.fileMultipliers.exists();
    }

    private boolean setupTempMultipliers() {
        this.fileTempMultipliers = new FileResource(this.getDataFolder(), "temporary_multipliers.yml");
        if (!this.fileTempMultipliers.exists())
            this.fileTempMultipliers.create();

        this.fileTempMultipliers.load();
        return this.fileTempMultipliers.exists();
    }
    private boolean setupPermMultipliers() {
        this.filePermMultipliers = new FileResource(this.getDataFolder(), "permission_multipliers.yml");
        if (!this.filePermMultipliers.exists())
            this.filePermMultipliers.create();

        this.filePermMultipliers.load();
        return this.filePermMultipliers.exists();
    }

    private boolean setupMines() {
        this.minesAPI = JetsPrisonMines.getInstance().getAPI();
        return this.minesAPI != null;
    }

    private boolean setupEconomy() {
        if (!this.getServer().getPluginManager().isPluginEnabled("Vault"))
            return false;

        RegisteredServiceProvider<Economy> registration = getServer().getServicesManager().getRegistration(Economy.class);
        if (registration == null)
            return false;

        this.vaultAPI = registration.getProvider();
        return true;
    }

    private boolean setupTokens() {
        return this.getServer().getPluginManager().isPluginEnabled("TokenEnchant");
    }
}
