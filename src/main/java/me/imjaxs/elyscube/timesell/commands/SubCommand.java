package me.imjaxs.elyscube.timesell.commands;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    public abstract String getPermission();
    public abstract String getUsage();
    public abstract boolean onCommand(CommandSender sender, String[] args);
}
