package cc.altoya.settlements;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import cc.altoya.settlements.Commands.Chunk.MainChunk;
import cc.altoya.settlements.Commands.Settlement.MainSettlement;
import cc.altoya.settlements.Events.EventProtectBlocks;
import cc.altoya.settlements.Events.EventProtectEntities;
import cc.altoya.settlements.Util.DatabaseUtil;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        initializeConfig();
        try {
            initializeDatabase();
        } catch (SQLException e) {
        }

        // How to register commands
        this.getCommand("chunk").setExecutor(new MainChunk());
        this.getCommand("settlement").setExecutor(new MainSettlement());

        // How to register eventListeners
        this.getServer().getPluginManager().registerEvents(new EventProtectBlocks(), this);
        this.getServer().getPluginManager().registerEvents(new EventProtectEntities(), this);

    }

    private void initializeDatabase() throws SQLException {
        DatabaseUtil.initializeConnection();

        String claimsTable = "CREATE TABLE `claims` (`id` INT PRIMARY KEY AUTO_INCREMENT,`uuid` VARCHAR(36) NOT NULL,`x` INT,`y` INT, `trusted` TEXT, UNIQUE KEY `unique_claim` (`uuid`, `x`, `y`))";
        String settlementsTable = "CREATE TABLE `settlements` (`id` INT PRIMARY KEY AUTO_INCREMENT,`name` TEXT,`description` TEXT,`uuids` TEXT, `invited_uuids` TEXT,`votes_ids` TEXT)";
        String votesTable = "CREATE TABLE `votes` (`id` INT PRIMARY KEY AUTO_INCREMENT,`action_id` INT,`allowed_voters` TEXT,`voted_list` TEXT,`yes_count` INT,`no_count` INT)";

        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(claimsTable)) {
            statement.executeUpdate();
        }

        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(settlementsTable)) {
            statement.executeUpdate();
        }

        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(votesTable)) {
            statement.executeUpdate();
        }
    }

    private void initializeConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (configFile.exists()) {
            return;
        }

        getConfig().addDefault("databasePort", "3306");
        getConfig().addDefault("databaseUrl", "0.0.0.0");
        getConfig().addDefault("databaseUsername", "root");
        getConfig().addDefault("databasePassword", "password");
        getConfig().addDefault("claimCountLimit", 9);
        getConfig().addDefault("claimChunkBoundary", 64);
        getConfig().addDefault("claimCloseByHowManyChunks", 1);

        getConfig().options().copyDefaults(true);
        saveConfig();
    }

}