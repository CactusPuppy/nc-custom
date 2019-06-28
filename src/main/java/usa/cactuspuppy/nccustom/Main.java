package usa.cactuspuppy.nccustom;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.annotation.permission.ChildPermission;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import usa.cactuspuppy.nccustom.utils.Config;
import usa.cactuspuppy.nccustom.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Plugin(name = "NCCustom", version = "0.1")
@Website("www.crashtc.com")
@Description("Custom plugin for the Noteblock Corner")
@Author("CactusPuppy")
@LogPrefix("NCCustom")
@ApiVersion(ApiVersion.Target.v1_13)
@Permission(name = "nccustom.*", desc = "Wildcard permission", children = {@ChildPermission(name = "nccustom.admin")})
@Permission(name = "nccustom.admin", desc = "Allows admin access", defaultValue = PermissionDefault.OP)
public class Main extends JavaPlugin {
    @Getter private static Main instance;
    @Getter private Config mainConfig;

    @Override
    public void onEnable() {
        long start = System.nanoTime();
        instance = this;
        if (!initPlugin()) {
            Logger.logSevere(this.getClass(), "Failure to initiate plugin, disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        long elapsedNanos = System.nanoTime() - start;
        Logger.logInfo(this.getClass(), ChatColor.GOLD + "NCCustom" + ChatColor.GREEN + " startup complete!");
        Logger.logInfo(this.getClass(), String.format(
                ChatColor.LIGHT_PURPLE + "Time Elapsed: " + ChatColor.AQUA + "%1$.2fms (%2$dns)",
                elapsedNanos / 10e6, elapsedNanos));
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void saveDefaultConfig() {
        // Do nothing, breaks custom config
    }

    public boolean initPlugin() {
        return configSetup() &&
                registerCommands();
    }

    private boolean registerCommands() {
        PluginCommand ncCustom = getCommand("nccustom");
        if (ncCustom == null) {
            Logger.logSevere(this.getClass(), "Could not obtain /nccustom command");
            return false;
        }

        return true;
    }

    private boolean configSetup() {
        //Get/create main config
        File dataFolder = Main.getInstance().getDataFolder();
        if (!dataFolder.isDirectory() && !dataFolder.mkdirs()) {
            Logger.logSevere(this.getClass(), "Could not find or create data folder.");
            return false;
        }
        File config = new File(Main.getInstance().getDataFolder(), "config.yml");
        //Create config if not exist
        if (!config.isFile()) {
            InputStream inputStream = getResource("config.yml");
            if (inputStream == null) {
                Logger.logSevere(this.getClass(), "No packaged config.yml?!");
                return false;
            }
            try {
                FileUtils.copyToFile(inputStream, config);
            } catch (IOException e) {
                Logger.logSevere(this.getClass(), "Error while creating new config", e);
                return false;
            }
        }
        //Set mainConfig
        mainConfig = new Config();
        return true;
    }

    //MockBukkit constructors
    public Main() {
        super();
    }

    protected Main(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }
}
