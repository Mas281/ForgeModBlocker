package me.itsmas.forgemodblocker.mods;

import com.google.common.collect.Lists;
import me.itsmas.forgemodblocker.ForgeModBlocker;
import me.itsmas.forgemodblocker.messaging.JoinListener;
import me.itsmas.forgemodblocker.messaging.MessageListener;
import me.itsmas.forgemodblocker.util.C;
import me.itsmas.forgemodblocker.util.Permission;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Handles caching of player mods
 */
public class ModManager
{
    public ModManager(ForgeModBlocker plugin)
    {
        new JoinListener(plugin);
        new MessageListener(plugin);

        Mode mode = EnumUtils.getEnum(Mode.class, plugin.getConfig().getString("mode").toUpperCase());

        if (mode == null)
        {
            mode = Mode.BLACKLIST;
        }

        this.mode = mode;

        this.blockForge = plugin.getConfig("block-forge", false);
        this.modList = plugin.getConfig("mod-list", new ArrayList<>());
        this.disallowedCommands = new ArrayList<>();

        List<String> disallowedCommands = plugin.getConfig("disallowed-mods-commands", Lists.newArrayList("kick %player% &cIllegal Mods - %disallowed_mods%"));
        disallowedCommands.forEach(C::colour);
    }

    /**
     * Plugin modes
     */
    private enum Mode
    {
        WHITELIST((mod, modList) -> modList.contains(mod)),
        BLACKLIST((mod, modList) -> !modList.contains(mod));

        Mode(BiFunction<String, List<String>, Boolean> function)
        {
            this.function = function;
        }

        private final BiFunction<String, List<String>, Boolean> function;

        /**
         * Determines whether a mod is allowed
         *
         * @param mod The mod to check
         * @param modList The mod list
         * @return Whether the mod is allowed
         */
        public boolean isAllowed(String mod, List<String> modList)
        {
            if (mod.equals("FML") || mod.equals("mcp") || mod.equals("Forge"))
            {
                return true;
            }

            return function.apply(mod, modList);
        }
    }

    /**
     * The mode the plugin is running in
     */
    private final Mode mode;

    /**
     * Determines whether a mod is disallowed
     *
     * @param mod The mod
     * @return Whether the mod is disallowed
     */
    private boolean isDisallowed(String mod)
    {
        return !mode.isAllowed(mod, modList);
    }

    /**
     * Whether to block all Forge clients
     */
    private final boolean blockForge;

    /**
     * The list of whitelisted/blacklisted mods
     */
    private final List<String> modList;

    /**
     * The commands to execute on players using disallowed mods
     */
    private final List<String> disallowedCommands;

    /**
     * Map of players to their {@link ModData} object
     */
    private final Map<Player, ModData> playerData = new HashMap<>();

    /**
     * Determines whether a player is using Forge or not
     *
     * @param player The player to check
     * @return Whether the player is using forge
     */
    public boolean isUsingForge(Player player)
    {
        return playerData.containsKey(player);
    }

    /**
     * Gets a player's {@link ModData} object
     *
     * @param player The player
     * @return The ModData object
     */
    public ModData getModData(Player player)
    {
        return playerData.get(player);
    }

    /**
     * Adds a player to the data map
     *
     * @see #playerData
     * @param player The player to add
     * @param data The player's {@link ModData}
     */
    public void addPlayer(Player player, ModData data)
    {
        playerData.put(player, data);

        checkForDisallowed(player, data.getMods());
    }

    /**
     * Checks whether a player is using any disallowed mods
     *
     * @param player The player
     * @param mods The player's mods
     */
    private void checkForDisallowed(Player player, Set<String> mods)
    {
        if (Permission.hasPermission(player, Permission.BYPASS))
        {
            return;
        }

        Set<String> disallowed = mods.stream().filter(this::isDisallowed).collect(Collectors.toSet());

        if (disallowed.size() > 0 || (mods.size() > 0 && blockForge))
        {
            // Player is using disallowed mods
            String modsString = String.join(", ", mods);
            String disallowedString = String.join(", ", disallowed);

            sendDisallowedCommand(player, modsString, disallowedString);
        }
    }

    /**
     * Sends the disallowed mods command for a player using illegal mods
     *
     * @param player The player
     * @param mods The player's mods as a string
     * @param disallowedMods The player's disallowed mods as a string
     */
    private void sendDisallowedCommand(Player player, String mods, String disallowedMods)
    {
        disallowedCommands.forEach(command ->
                Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    command
                            .replace("%player%", player.getName())
                            .replace("%mods%", mods)
                            .replace("%disallowed_mods%", disallowedMods)
            )
        );
    }

    /**
     * Removes a player from the data map
     *
     * @see #playerData
     * @param player The player to remove
     */
    public void removePlayer(Player player)
    {
        playerData.remove(player);
    }
}
