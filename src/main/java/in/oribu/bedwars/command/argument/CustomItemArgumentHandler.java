package in.oribu.bedwars.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import in.oribu.bedwars.item.CustomItem;
import in.oribu.bedwars.item.ItemRegistry;

import java.util.List;

public class CustomItemArgumentHandler extends RoseCommandArgumentHandler<CustomItem> {

    public CustomItemArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, CustomItem.class);
    }

    @Override
    protected CustomItem handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) throws HandledArgumentException {
        final String input = argumentParser.next();

        final CustomItem customItem = ItemRegistry.get(input);
        if (customItem != null)
            return customItem;

        throw new HandledArgumentException("command-item-invalid-item", StringPlaceholders.of("input", input));
    }

    @Override
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        argumentParser.next();

        return ItemRegistry.getItems().keySet().stream().toList();
    }

}
