package in.oribu.bedwars.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.manager.ShopManager;
import in.oribu.bedwars.shop.Shop;

import java.util.List;

public class ShopArgumentHandler extends ArgumentHandler<Shop> {

    public ShopArgumentHandler() {
        super(Shop.class);
    }

    @Override
    public Shop handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
        Shop shop = BedwarsPlugin.get().getManager(ShopManager.class).getShops().get(input.toLowerCase());
        if (shop != null)
            return shop;

        throw new HandledArgumentException("command-item-invalid-shop", StringPlaceholders.of("input", input));
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return BedwarsPlugin.get().getManager(ShopManager.class).getShops().keySet().stream().toList();
    }

}
