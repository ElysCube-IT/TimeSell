package me.imjaxs.elyscube.timesell.commands;

import com.google.common.collect.Maps;
import me.imjaxs.elyscube.timesell.TimeSell;
import me.imjaxs.elyscube.timesell.commands.sub.AddItemCommand;
import me.imjaxs.elyscube.timesell.commands.sub.DelItemCommand;
import me.imjaxs.elyscube.timesell.tools.file.FileResource;
import me.imjaxs.elyscube.timesell.tools.message.ChatMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class CommandManager implements CommandExecutor {
    private final FileResource messages = TimeSell.getInstance().getFileMessages();
    private final Map<String, SubCommand> subCommands = Maps.newHashMap();

    public CommandManager() {
        this.subCommands.put("additem", new AddItemCommand());
        this.subCommands.put("delitem", new DelItemCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("timesell.use")) {
            String message = this.messages.getFileConfiguration().getString("no-permission");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(sender);
            return true;
        }

        if (args.length == 0) {
            // CREDITS
            return true;
        }

        SubCommand subCommand = this.subCommands.get(args[0].toLowerCase());
        if (subCommand == null) {
            String message = this.messages.getFileConfiguration().getString("no-subcommand");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(sender);
            return true;
        }

        if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
            String message = this.messages.getFileConfiguration().getString("no-permission");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(sender);
            return true;
        }

        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args,1, subArgs, 0, subArgs.length);

        if (!subCommand.onCommand(sender, subArgs)) {
            String message = this.messages.getFileConfiguration().getString("no-correct-usage");
            if (message != null && !message.isEmpty())
                new ChatMessage(message)
                        .replace("%command_usage%", subCommand.getUsage()).send(sender);
            return true;
        }
        return true;
    }
}
