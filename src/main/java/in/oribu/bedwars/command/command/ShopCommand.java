package in.oribu.bedwars.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.HexUtils;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import in.oribu.bedwars.shop.Shop;
import in.oribu.bedwars.shop.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopCommand extends RoseCommand {

    public ShopCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    @SuppressWarnings("deprecation")
    public void execute(CommandContext context, Shop shop) {
        Player player = (Player) context.getSender();
        PaginatedGui gui = shop.create();

        Map<Integer, ShopItem> items = shop.getItems();
        for (ShopItem shopItem : shop.getItems().values()) {
            if (shopItem == null || shopItem.getItem() == null || shopItem.getItem().getType().isAir()) continue;

            ItemStack toModify = shopItem.getItem().clone();

            toModify.editMeta(meta -> {
                List<String> lore = new ArrayList<>(meta.getLore() == null ? new ArrayList<>() : meta.getLore());
                lore.add(HexUtils.colorify("&fCost: :3"));
                shopItem.getCost().forEach((material, amount) -> lore.add(HexUtils.colorify(" &7| &f" + material.name() + ": &bx" + amount)));
                meta.setLore(lore);
            });

            gui.addItem(new GuiItem(toModify, event -> shopItem.buy(player)));
        }

        gui.open(player);
    }

    @Override
    protected String getDefaultName() {
        return "shop";
    }

    @Override
    public String getDescriptionKey() {
        return "command-shop-description";
    }

    @Override
    public String getRequiredPermission() {
        return "bedwars.shop";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

}
