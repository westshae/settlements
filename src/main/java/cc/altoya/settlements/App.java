package cc.altoya.settlements;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import cc.altoya.settlements.Alliance.AllianceTabCompleter;
import cc.altoya.settlements.Alliance.EventAllianceChat;
import cc.altoya.settlements.Alliance.EventAllianceOnJoin;
import cc.altoya.settlements.Alliance.MainAlliance;
import cc.altoya.settlements.Blueprint.BlueprintTabCompleter;
import cc.altoya.settlements.Blueprint.MainBlueprint;
import cc.altoya.settlements.Build.BuildTabCompleter;
import cc.altoya.settlements.Build.EventBreakResourceBlock;
import cc.altoya.settlements.Build.EventEnterStructureChunk;
import cc.altoya.settlements.Build.MainBuild;
import cc.altoya.settlements.Build.ScheduledCleanUp;
import cc.altoya.settlements.City.CityTabCompleter;
import cc.altoya.settlements.City.EventFoundCity;
import cc.altoya.settlements.City.EventGriefPrevention;
import cc.altoya.settlements.City.EventProtectBlocks;
import cc.altoya.settlements.City.EventProtectEntities;
import cc.altoya.settlements.City.MainCity;
import cc.altoya.settlements.City.ScheduledCollection;
import cc.altoya.settlements.Map.EventMapUpdates;
import cc.altoya.settlements.Map.MainMap;
import cc.altoya.settlements.Map.MapTabCompleter;


public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        initializeConfig();

        // Register commands
        this.getCommand("city").setExecutor(new MainCity());
        this.getCommand("alliance").setExecutor(new MainAlliance());
        this.getCommand("build").setExecutor(new MainBuild());
        this.getCommand("blueprint").setExecutor(new MainBlueprint());
        this.getCommand("map").setExecutor(new MainMap());

        //Register tab completers
        this.getCommand("city").setTabCompleter(new CityTabCompleter());
        this.getCommand("alliance").setTabCompleter(new AllianceTabCompleter());
        this.getCommand("build").setTabCompleter(new BuildTabCompleter());
        this.getCommand("blueprint").setTabCompleter(new BlueprintTabCompleter());
        this.getCommand("map").setTabCompleter(new MapTabCompleter());

        //Register eventListeners
        this.getServer().getPluginManager().registerEvents(new EventProtectBlocks(), this);
        this.getServer().getPluginManager().registerEvents(new EventProtectEntities(), this);
        this.getServer().getPluginManager().registerEvents(new EventAllianceChat(), this);
        this.getServer().getPluginManager().registerEvents(new EventGriefPrevention(), this);
        this.getServer().getPluginManager().registerEvents(new EventEnterStructureChunk(), this);
        this.getServer().getPluginManager().registerEvents(new EventAllianceOnJoin(), this);
        this.getServer().getPluginManager().registerEvents(new EventFoundCity(), this);
        this.getServer().getPluginManager().registerEvents(new EventBreakResourceBlock(), this);
        this.getServer().getPluginManager().registerEvents(new EventMapUpdates(), this);

        //Register runnables
        this.getServer().getScheduler().runTask(this, ScheduledCleanUp.getRunnable());//Run instantly, once.
        this.getServer().getScheduler().runTask(this, ScheduledCollection.getRunnable());//Run instantly, internally rerun.
    }

    private void initializeConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }
}
