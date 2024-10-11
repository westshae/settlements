package cc.altoya.settlements;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import cc.altoya.settlements.Alliance.AllianceTabCompleter;
import cc.altoya.settlements.Alliance.EventAllianceChat;
import cc.altoya.settlements.Alliance.MainAlliance;
import cc.altoya.settlements.Blueprint.BlueprintTabCompleter;
import cc.altoya.settlements.Blueprint.MainBlueprint;
import cc.altoya.settlements.Build.BuildTabCompleter;
import cc.altoya.settlements.Build.EventInteractStructure;
import cc.altoya.settlements.Build.EventBreakStructureBlock;
import cc.altoya.settlements.Build.MainBuild;
import cc.altoya.settlements.Build.ScheduledCleanUp;
import cc.altoya.settlements.Build.ScheduledWorker;
import cc.altoya.settlements.City.CityTabCompleter;
import cc.altoya.settlements.City.EventProtectBlocks;
import cc.altoya.settlements.City.EventProtectEntities;
import cc.altoya.settlements.City.MainCity;


public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        initializeConfig();

        // Register commands
        this.getCommand("city").setExecutor(new MainCity());
        this.getCommand("alliance").setExecutor(new MainAlliance());
        this.getCommand("build").setExecutor(new MainBuild());
        this.getCommand("blueprint").setExecutor(new MainBlueprint());

        //Register tab completers
        this.getCommand("city").setTabCompleter(new CityTabCompleter());
        this.getCommand("alliance").setTabCompleter(new AllianceTabCompleter());
        this.getCommand("build").setTabCompleter(new BuildTabCompleter());
        this.getCommand("blueprint").setTabCompleter(new BlueprintTabCompleter());

        //Register eventListeners
        this.getServer().getPluginManager().registerEvents(new EventProtectBlocks(), this);
        this.getServer().getPluginManager().registerEvents(new EventProtectEntities(), this);
        this.getServer().getPluginManager().registerEvents(new EventAllianceChat(), this);
        this.getServer().getPluginManager().registerEvents(new EventBreakStructureBlock(), this);
        this.getServer().getPluginManager().registerEvents(new EventInteractStructure(), this);

        //Register runnables
        this.getServer().getScheduler().runTaskTimer(this, ScheduledWorker.getRunnable(), 200L, 100L);//Wait 200 ticks (10s) then run every 100 ticks (5s)
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
