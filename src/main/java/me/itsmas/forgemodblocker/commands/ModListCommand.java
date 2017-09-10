package me.itsmas.forgemodblocker.commands;

import me.itsmas.forgemodblocker.ForgeModBlocker;
import me.itsmas.forgemodblocker.mods.ModData;
import me.itsmas.forgemodblocker.mods.ModManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class ModListCommand implements CommandExecutor
{
    private ForgeModBlocker plugin;

    public ModListCommand(ForgeModBlocker plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args)
    {
        Player p;
        if(args.length < 1)
        {
            if(cs instanceof Player)
            {
                p = (Player) cs;
            } else
                {
                cs.sendMessage(ChatColor.RED + "You must be a player to do this!");
                return true;
            }
        }
        else
        {
            p = Bukkit.getPlayer(args[0]);
        }

        if(p == null)
        {
            cs.sendMessage(ChatColor.RED + "Cannot find player");
            return true;
        }

        ModManager manager = plugin.getModManager();

        if(!manager.isUsingForge(p))
        {
            cs.sendMessage(ChatColor.GREEN + p.getName() + " is not using Forge.");
            return true;
        }

        Set<String> mods = manager.getModData(p).getMods();

        if(mods.isEmpty()) {
            cs.sendMessage(ChatColor.GREEN + p.getName() + " is using Forge, but does not have any mods installed.");
        }
        else
        {
            cs.sendMessage(ChatColor.GREEN + p.getName() + " is using the following mods: " +
                    ChatColor.LIGHT_PURPLE + String.join(", ", mods));
        }

        return true;
    }
}
