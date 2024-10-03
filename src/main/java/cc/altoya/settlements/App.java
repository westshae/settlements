package cc.altoya.settlements;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import cc.altoya.settlements.Commands.Alliance.MainAlliance;
import cc.altoya.settlements.Commands.Domain.MainDomain;
import cc.altoya.settlements.Commands.Structure.MainStructure;
import cc.altoya.settlements.Events.EventAllianceChat;
import cc.altoya.settlements.Events.EventProtectBlocks;
import cc.altoya.settlements.Events.EventProtectEntities;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        initializeConfig();

        // Register commands
        this.getCommand("domain").setExecutor(new MainDomain());
        this.getCommand("alliance").setExecutor(new MainAlliance());
        this.getCommand("structure").setExecutor(new MainStructure());

        // How to register eventListeners
        this.getServer().getPluginManager().registerEvents(new EventProtectBlocks(), this);
        this.getServer().getPluginManager().registerEvents(new EventProtectEntities(), this);
        this.getServer().getPluginManager().registerEvents(new EventAllianceChat(), this);
    }

    private void initializeConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }
}
