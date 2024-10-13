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
import cc.altoya.settlements.Build.EventInteractStructure;
import cc.altoya.settlements.Build.EventBreakStructureBlock;
import cc.altoya.settlements.Build.EventEnterStructureChunk;
import cc.altoya.settlements.Build.MainBuild;
import cc.altoya.settlements.Build.ScheduledCleanUp;
import cc.altoya.settlements.Build.ScheduledWorker;
import cc.altoya.settlements.City.CityTabCompleter;
import cc.altoya.settlements.City.EventFoundCity;
import cc.altoya.settlements.City.EventGriefPrevention;
import cc.altoya.settlements.City.EventProtectBlocks;
import cc.altoya.settlements.City.EventProtectEntities;
import cc.altoya.settlements.City.MainCity;
import cc.altoya.settlements.Item.ItemTabCompleter;
import cc.altoya.settlements.Item.MainItem;


public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        initializeConfig();

        // Register commands
        this.getCommand("city").setExecutor(new MainCity());
        this.getCommand("alliance").setExecutor(new MainAlliance());
        this.getCommand("build").setExecutor(new MainBuild());
        this.getCommand("blueprint").setExecutor(new MainBlueprint());
        this.getCommand("item").setExecutor(new MainItem());

        //Register tab completers
        this.getCommand("city").setTabCompleter(new CityTabCompleter());
        this.getCommand("alliance").setTabCompleter(new AllianceTabCompleter());
        this.getCommand("build").setTabCompleter(new BuildTabCompleter());
        this.getCommand("blueprint").setTabCompleter(new BlueprintTabCompleter());
        this.getCommand("item").setTabCompleter(new ItemTabCompleter());

        //Register eventListeners
        this.getServer().getPluginManager().registerEvents(new EventProtectBlocks(), this);
        this.getServer().getPluginManager().registerEvents(new EventProtectEntities(), this);
        this.getServer().getPluginManager().registerEvents(new EventAllianceChat(), this);
        this.getServer().getPluginManager().registerEvents(new EventBreakStructureBlock(), this);
        this.getServer().getPluginManager().registerEvents(new EventInteractStructure(), this);
        this.getServer().getPluginManager().registerEvents(new EventGriefPrevention(), this);
        this.getServer().getPluginManager().registerEvents(new EventEnterStructureChunk(), this);
        this.getServer().getPluginManager().registerEvents(new EventAllianceOnJoin(), this);
        this.getServer().getPluginManager().registerEvents(new EventFoundCity(), this);

        //Register runnables
        this.getServer().getScheduler().runTaskTimer(this, ScheduledWorker.getRunnable(), 200L, 300L);//Wait 200 ticks (10s) then run every 300 ticks (15s)
        this.getServer().getScheduler().runTask(this, ScheduledCleanUp.getRunnable());//Run instantly, once.
    }

    private void initializeConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }
}
