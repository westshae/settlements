package cc.altoya.settlements;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import cc.altoya.settlements.Commands.Alliance.MainAlliance;
import cc.altoya.settlements.Commands.Domain.MainDomain;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        initializeConfig();

        // Register commands
        this.getCommand("domain").setExecutor(new MainDomain());
        this.getCommand("alliance").setExecutor(new MainAlliance());
    }

    private void initializeConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }
}
