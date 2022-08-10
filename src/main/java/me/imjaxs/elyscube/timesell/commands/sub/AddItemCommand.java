package me.imjaxs.elyscube.timesell.commands.sub;

import me.imjaxs.elyscube.timesell.TimeSell;
import me.imjaxs.elyscube.timesell.commands.SubCommand;
import me.imjaxs.elyscube.timesell.handlers.ShopHandler;
import me.imjaxs.elyscube.timesell.tools.file.FileResource;
import me.imjaxs.elyscube.timesell.tools.message.ChatMessage;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddItemCommand extends SubCommand {
    private final FileResource messages = TimeSell.getInstance().getFileMessages();
    private final ShopHandler shops = TimeSell.getInstance().getShops();

    @Override
    public String getPermission() {
        return "timesell.use.additem";
    }

    @Override
    public String getUsage() {
        return "/timesell additem <price>";
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

        if (args.length != 1)
            return false;

        ItemStack itemStack = player.getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR || !itemStack.getType().isBlock()) {
            String message = this.messages.getFileConfiguration().getString("no-block-in-hand");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(sender);
            return true;
        }

        if (this.shops.hasShopBlock(itemStack)) {
            String message = this.messages.getFileConfiguration().getString("block-is-already-in-shop");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(sender);
            return true;
        }

        if (!NumberUtils.isNumber(args[0]))
            return false;
        double price = Double.parseDouble(args[0]);

        this.shops.addShopBlock(itemStack, price);

        String message = this.messages.getFileConfiguration().getString("add-block-in-shop");
        if (message != null && !message.isEmpty())
            new ChatMessage(message)
                    .replace("%block%", itemStack.getType() + ":" + itemStack.getDurability()).send(sender);
        return true;
    }
}
