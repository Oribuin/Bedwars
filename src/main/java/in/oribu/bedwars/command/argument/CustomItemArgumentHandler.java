package in.oribu.bedwars.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import in.oribu.bedwars.item.CustomItem;
import in.oribu.bedwars.item.ItemRegistry;

import java.util.List;

public class CustomItemArgumentHandler extends ArgumentHandler<CustomItem> {

    public CustomItemArgumentHandler() {
        super(CustomItem.class);
    }

    @Override
    public CustomItem handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
        CustomItem customItem = ItemRegistry.get(input.toLowerCase());
        if (customItem == null)
            throw new HandledArgumentException("command-item-invalid-item", StringPlaceholders.of("input", input));

        return customItem;
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return ItemRegistry.getItems().keySet().stream().toList();
    }

}
