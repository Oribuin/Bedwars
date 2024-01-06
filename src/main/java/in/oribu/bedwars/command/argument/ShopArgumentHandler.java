package in.oribu.bedwars.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import in.oribu.bedwars.manager.ShopManager;
import in.oribu.bedwars.shop.Shop;

import java.util.List;

public class ShopArgumentHandler extends RoseCommandArgumentHandler<Shop> {

    public ShopArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, Shop.class);
    }

    @Override
    protected Shop handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) throws HandledArgumentException {
        String input = argumentParser.next();

        Shop shop = this.rosePlugin.getManager(ShopManager.class).getShops().get(input);
        if (shop != null)
            return shop;

        throw new HandledArgumentException("command-item-invalid-shop", StringPlaceholders.of("input", input));
    }

    @Override
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        argumentParser.next();

        return this.rosePlugin.getManager(ShopManager.class).getShops().keySet().stream().toList();
    }

}
