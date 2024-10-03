package cc.altoya.settlements.Util;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class StructureUtil {
    public static FileConfiguration getStructureConfig() {
        return GeneralUtil.getPluginConfig("settlements", "structures.yml");
    }

    public static void saveStructureConfig(FileConfiguration config) {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(),
                "structures.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
