package me.imjaxs.elyscube.timesell.commands.sub;

import me.imjaxs.elyscube.timesell.TimeSell;
import me.imjaxs.elyscube.timesell.commands.SubCommand;
import me.imjaxs.elyscube.timesell.handlers.ShopHandler;
import me.imjaxs.elyscube.timesell.tools.file.FileResource;
import me.imjaxs.elyscube.timesell.tools.message.ChatMessage;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DelItemCommand extends SubCommand {
    private final FileResource messages = TimeSell.getInstance().getFileMessages();
    private final ShopHandler shops = TimeSell.getInstance().getShops();

    @Override
    public String getPermission() {
        return "timesell.use.delitem";
    }

    @Override
    public String getUsage() {
        return "/timesell delitem";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            String message = this.messages.getFileConfiguration().getString("no-console");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(sender);
            return true;
        }
        Player player = (Player) sender;

        if (args.length != 0)
            return false;

        ItemStack itemStack = player.getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR || !itemStack.getType().isBlock()) {
            String message = this.messages.getFileConfiguration().getString("no-block-in-hand");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(sender);
            return true;
        }

        if (!this.shops.hasShopBlock(itemStack)) {
            String message = this.messages.getFileConfiguration().getString("block-not-is-in-shop");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(sender);
            return true;
        }

        this.shops.removeShopBlock(itemStack);

        String message = this.messages.getFileConfiguration().getString("remove-block-in-shop");
        if (message != null && !message.isEmpty())
            new ChatMessage(message)
                    .replace("%block%", itemStack.getType() + ":" + itemStack.getDurability()).send(sender);
        return true;
    }
}
