package in.oribu.bedwars.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.manager.GameManager;
import in.oribu.bedwars.match.Level;

import java.util.List;

public class LevelArgumentHandler extends ArgumentHandler<Level> {

    public LevelArgumentHandler() {
        super(Level.class);
    }

    @Override
    public Level handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
        Level shop = BedwarsPlugin.get().getManager(GameManager.class).getLevels().get(input.toLowerCase());
        if (shop != null)
            return shop;

        throw new HandledArgumentException("command-item-invalid-level", StringPlaceholders.of("input", input));
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return BedwarsPlugin.get().getManager(GameManager.class).getLevels().keySet().stream().toList();
    }

}
