package in.oribu.bedwars.shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public class ShopItem {

    private final String name;
    private ItemStack item;
    private Map<ItemStack, Integer> cost;
    private int amount;

    public ShopItem(String name, ItemStack item, Map<ItemStack, Integer> cost) {
        this.name = name;
        this.cost = cost;
        this.item = item;
        this.amount = 1;
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
        final PlayerInventory inv = buyer.getInventory();
        final ItemStack[] contents = inv.getStorageContents();

        for (final Map.Entry<ItemStack, Integer> entry : this.cost.entrySet()) {
            final ItemStack item = entry.getKey(); // Hope this is not being modified

            for (int i = 0; i < contents.length; i++) {
                final ItemStack content = contents[i];

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
        final PlayerInventory inv = buyer.getInventory();

        // Check if the player has enough resources to buy the item.
        for (final Map.Entry<ItemStack, Integer> entry : this.cost.entrySet()) {
            final ItemStack item = entry.getKey();
            final int amount = entry.getValue();

            if (!inv.containsAtLeast(item, amount))
                return false;
        }

        return true;
    }

    public String getName() {
        return this.name;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Map<ItemStack, Integer> getCost() {
        return this.cost;
    }

    public void setCost(Map<ItemStack, Integer> cost) {
        this.cost = cost;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


}
