package me.itsmas.forgemodblocker.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.itsmas.forgemodblocker.ForgeModBlocker;
import me.itsmas.forgemodblocker.mods.ModData;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

/**
 * Handles {@link PlaceholderAPI} hooks
 */
public class Placeholders extends EZPlaceholderHook
{
    /**
     * The plugin instance
     */
    private final ForgeModBlocker plugin;

    public Placeholders(ForgeModBlocker plugin)
    {
        super(plugin, "forgemodblocker");

        this.plugin = plugin;

        hook();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier)
    {
        if (player == null)
        {
            return null;
        }

        if (identifier.equals("forge"))
        {
            return Boolean.toString(plugin.getModManager().isUsingForge(player));
        }

        if (identifier.equals("mods"))
        {
            ModData data = plugin.getModManager().getModData(player);

            return data == null ? "" : data.getMods().stream().collect(Collectors.joining(", "));
        }

        return null;
    }
}
