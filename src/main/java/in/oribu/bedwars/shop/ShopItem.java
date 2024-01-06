package in.oribu.bedwars.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public class ShopItem {

    private ItemStack item;
    private Map<Material, Integer> cost;

    public ShopItem(ItemStack item, Map<Material, Integer> cost) {
        this.cost = cost;
        this.item = item;
    }

    /**
     * Buy the item from the shop for the player.
     *
     * @param buyer The player buying the item.
     */
    public void buy(Player buyer) {
        // Check if the player has enough resources to buy the item.
        // TODO: Can't buy message
        if (!this.canBuy(buyer)) {
            buyer.sendMessage("You can't buy this item!");
            return;
        }

        // Remove the resources from the player's inventory.
        PlayerInventory inv = buyer.getInventory();
        ItemStack[] contents = inv.getStorageContents();

        for (final Map.Entry<Material, Integer> entry : this.cost.entrySet()) {
            ItemStack item = new ItemStack(entry.getKey()); // Hope this is not being modified
            int amount = entry.getValue();

            for (int i = 0; i < contents.length; i++) {
                ItemStack content = contents[i];

                if (content == null || content.getType().isAir()) continue;
                if (!content.isSimilar(item)) continue;

                if (content.getAmount() > amount) {
                    content.setAmount(content.getAmount() - amount);
                    amount = 0;
                } else {
                    amount -= content.getAmount();
                    contents[i] = null;
                }

                if (amount == 0)
                    break;
            }
        }

    }

    /**
     * Check if the player can buy the item.
     *
     * @param buyer The player
     * @return If the player can buy the item
     */
    public boolean canBuy(Player buyer) {
        PlayerInventory inv = buyer.getInventory();

        // Check if the player has enough resources to buy the item.
        for (final Map.Entry<Material, Integer> entry : this.cost.entrySet()) {
            Material item = entry.getKey();
            int amount = entry.getValue();

            if (!inv.contains(item, amount))
                return false;
        }

        return true;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Map<Material, Integer> getCost() {
        return cost;
    }

    public void setCost(Map<Material, Integer> cost) {
        this.cost = cost;
    }

}
