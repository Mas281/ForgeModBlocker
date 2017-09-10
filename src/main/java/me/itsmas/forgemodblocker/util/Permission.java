package me.itsmas.forgemodblocker.util;

import org.bukkit.entity.Player;

/**
 * Permission nodes for the plugin
 */
public enum Permission
{
    ALL("*"),
    UPDATE_NOTIFICATION("update_notification"),
    BYPASS("bypass");

    Permission(String name)
    {
        this.node = "fmb." + name;
    }

    /**
     * The permission's node
     */
    private final String node;

    /**
     * Determines whether a player has a {@link Permission}
     *
     * @param player The player
     * @param permission The permission
     * @return Whether the player has the permission
     */
    public static boolean hasPermission(Player player, Permission permission)
    {
        return player.hasPermission(permission.node);
    }
}
