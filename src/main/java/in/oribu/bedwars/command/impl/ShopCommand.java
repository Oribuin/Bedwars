package in.oribu.bedwars.command.impl;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.HexUtils;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import in.oribu.bedwars.command.argument.ShopArgumentHandler;
import in.oribu.bedwars.shop.Shop;
import in.oribu.bedwars.shop.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopCommand extends BaseRoseCommand {

    public ShopCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    @SuppressWarnings("deprecation")
    public void execute(CommandContext context) {
        Player player = (Player) context.getSender();
        Shop shop = context.get("shop");
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
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("shop")
                .descriptionKey("command-shop-description")
                .permission("bedwars.shop")
                .playerOnly(true)
                .arguments(this.createArgumentsDefinition())
                .build();
    }


    public ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .required("shop", new ShopArgumentHandler())
                .build();
    }

}
