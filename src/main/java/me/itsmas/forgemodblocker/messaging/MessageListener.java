package me.itsmas.forgemodblocker.messaging;

import me.itsmas.forgemodblocker.ForgeModBlocker;
import me.itsmas.forgemodblocker.mods.ModData;
import me.itsmas.forgemodblocker.util.UtilServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Listens to Forge messages through the messaging channel
 */
public class MessageListener implements PluginMessageListener
{
    /**
     * The plugin instance
     */
    private final ForgeModBlocker plugin;

    public MessageListener(ForgeModBlocker plugin)
    {
        this.plugin = plugin;

        UtilServer.registerIncomingChannel("FML|HS", this);
    }

    /**
     * Weird characters which need replacing from the raw mod string
     */
    private final String[] weirdChars = {"\u0002", "\u000B", "\u0011", "\u0005", "\u0003", "\u0007", "\u0004", "\u001B", "\u0009", "\f", "\r", "\t", "\n"};

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] data)
    {
        // ModList is the only data sent by forge with >2 length
        if (data.length > 2)
        {
            // Mod data
            String modString = new String(data);
            modString = normalizeString(modString);

            ModData modData = getModData(modString);
            plugin.getModManager().addPlayer(player, modData);
        }
    }

    /**
     * Normalizes the mod message string
     *
     * @param string The input
     * @return The normalized string
     */
    private String normalizeString(String string)
    {
        for (String character : weirdChars)
        {
            string = string.replace(character, " ");
        }

        return string.trim();
    }

    /**
     * Fetches a {@link ModData} object from the raw mods string
     *
     * @param string The input
     * @return A ModData object
     */
    private ModData getModData(String string)
    {
        Map<String, String> mods = new HashMap<>();
        String lastMod = "";

        int i = -1;
        for (String info : string.split(" "))
        {
            i++;

            if (i % 2 == 0)
            {
                lastMod = info;
            }
            else
            {
                mods.put(lastMod, info);
            }
        }

        return new ModData(mods);
    }
}
