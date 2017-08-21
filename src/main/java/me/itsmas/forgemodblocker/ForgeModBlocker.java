package me.itsmas.forgemodblocker;

import me.itsmas.forgemodblocker.metrics.Metrics;
import me.itsmas.forgemodblocker.mods.ModManager;
import me.itsmas.forgemodblocker.placeholder.Placeholders;
import me.itsmas.forgemodblocker.update.Updater;
import me.itsmas.forgemodblocker.util.C;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the ForgeModBlocker plugin
 *
 * @author Mas281
 * @version 1.2.3
 * @since 1.0
 */
public class ForgeModBlocker extends JavaPlugin
{
    private ModManager modManager;

    @Override
    public void onEnable()
    {
        saveDefaultConfig();

        C.setPrefix();

        boolean placeholderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

        if (placeholderAPI)
        {
            new Placeholders(this);
        }

        new Metrics(this).addCustomChart(new Metrics.SimplePie("using_placeholderapi", () -> Boolean.toString(placeholderAPI)));
        new Updater(this);

        modManager = new ModManager(this);
    }

    /**
     * Fetches a value from the config
     *
     * @param path The path to the value
     * @param defaultValue The default value to return
     * @return The value at the path
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfig(String path, Object defaultValue)
    {
        return (T) getConfig().get(path, defaultValue);
    }

    public ModManager getModManager()
    {
        return modManager;
    }
}
