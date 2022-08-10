package me.imjaxs.elyscube.timesell.listeners;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;
import me.imjaxs.elyscube.timesell.TimeSell;
import me.imjaxs.elyscube.timesell.handlers.MultiplierHandler;
import me.imjaxs.elyscube.timesell.handlers.ShopHandler;
import me.jet315.prisonmines.JetsPrisonMinesAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlockListener implements Listener {
    private final JetsPrisonMinesAPI minesAPI = TimeSell.getInstance().getMinesAPI();
    private final Economy vaultAPI = TimeSell.getInstance().getVaultAPI();

    private final ShopHandler shops = TimeSell.getInstance().getShops();
    private final MultiplierHandler multipliers = TimeSell.getInstance().getMultipliers();

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = event.getPlayer().getItemInHand();
        if (itemStack == null)
            return;

        Block block = event.getBlock();
        if (this.minesAPI.getMinesByBlock(block).isEmpty())
            return;

        ItemStack blockItem = new ItemStack(block.getType(), 1, block.getData());
        if (!this.shops.hasShopBlock(blockItem))
            return;
        double price = this.shops.getShopBlock(blockItem);

        int fortune = 1;
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS))
            fortune += itemStack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        double multiplier = this.multipliers.getAllMultipliers(player);

        block.setType(Material.AIR);
        this.vaultAPI.depositPlayer(player, (price * fortune) * multiplier);
    }

    @EventHandler
    public void a(TEBlockExplodeEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = event.getPlayer().getItemInHand();
        if (itemStack == null)
            return;

        Block block = event.getBlock();
        if (this.minesAPI.getMinesByBlock(block).isEmpty())
            return;

        double price = 0.0;
        List<Block> blocks = new ArrayList<>();

        ItemStack blockItem = new ItemStack(block.getType(), 1, block.getData());
        if (this.shops.hasShopBlock(blockItem)) {
            price += this.shops.getShopBlock(blockItem);
            blocks.add(block);
        }

        for (Block var : event.blockList()) {
            ItemStack var1 = new ItemStack(var.getType(), 1, var.getData());
            if (!this.shops.hasShopBlock(var1))
                continue;

            price += this.shops.getShopBlock(var1);
            blocks.add(var);
        }

        int fortune = 1;
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS))
            fortune += itemStack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        double multiplier = this.multipliers.getAllMultipliers(player);

        event.getDrops().clear();
        blocks.forEach(var -> var.setType(Material.AIR));

        this.vaultAPI.depositPlayer(player, (price * fortune) * multiplier);
    }
}
