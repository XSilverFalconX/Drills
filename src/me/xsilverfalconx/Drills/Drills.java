package me.xsilverfalconx.Drills;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Drills
        extends JavaPlugin {

    public Permission drilluse = new Permission("Drills.use");
    public Logger logger;
    private PluginDescriptionFile pdfFile;

    public void onDisable() {
        pdfFile = getDescription();
        logger.info(pdfFile.getName() + " Is now disabled.");
    }

    public void onEnable() {
        logger = getLogger();
        pdfFile = getDescription();
        logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " Is now Enabled. ");
        PluginManager pm = getServer().getPluginManager();
        pm.addPermission(drilluse);
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        if (new File(getDataFolder(), "config.yml").exists()) {
            logger.info("Config Found... Reloading");
            reloadConfig();
        } else {
            logger.info("Creating Config...");
            saveDefaultConfig();
            reloadConfig();
            logger.info("Done!");
        }
    }
}
