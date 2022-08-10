package me.imjaxs.elyscube.timesell.handlers;

import com.google.common.collect.Maps;
import me.imjaxs.elyscube.timesell.TimeSell;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopHandler {
    private final TimeSell plugin = TimeSell.getInstance();
    private final Map<ItemStack, Double> shopPrices = Maps.newHashMap();

    public void addShopBlock(ItemStack itemStack, double price) {
        this.shopPrices.put(itemStack, price);
        this.saveItems();
    }

    public void removeShopBlock(ItemStack itemStack) {
        ItemStack var = null;
        for (ItemStack var1 : this.shopPrices.keySet())
            if (var1.isSimilar(itemStack)) var = var1;
        this.shopPrices.remove(var);

        this.saveItems();
    }

    public boolean hasShopBlock(ItemStack itemStack) {
        return this.shopPrices.keySet().stream().anyMatch(var -> var.isSimilar(itemStack));
    }

    public double getShopBlock(ItemStack itemStack) {
        return this.shopPrices.keySet().stream().filter(var -> var.isSimilar(itemStack)).findFirst().map(this.shopPrices::get).orElse(-1.0);
    }

    public void loadFromFile() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
           List<String> shopItems = plugin.getConfig().getStringList("items");
           if (shopItems == null || shopItems.isEmpty())
               return;

           shopItems.forEach(item -> {
               String[] items = item.split(";");

               String[] itemData = items[0].split(":");
               this.shopPrices.put(new ItemStack(Material.valueOf(itemData[0].toUpperCase()), 1, Short.parseShort(itemData[1])), Double.parseDouble(items[1]));
           });
        });
    }

    private void saveItems() {
        List<String> strings = new ArrayList<>();
        this.shopPrices.forEach((itemStack, price) -> strings.add(itemStack.getType() + ":" + itemStack.getDurability() + ";" + price));

        plugin.getConfig().set("items", strings);
        plugin.saveConfig();
    }
}
