package in.oribu.bedwars.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;

import java.util.List;

public class BedwarsCommandWrapper extends RoseCommandWrapper {

    public BedwarsCommandWrapper(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public String getDefaultName() {
        return "bedwars";
    }

    @Override
    public List<String> getDefaultAliases() {
        return List.of("bw");
    }

    @Override
    public List<String> getCommandPackages() {
        return List.of("in.oribu.bedwars.command.command");
    }

    @Override
    public boolean includeBaseCommand() {
        return true;
    }

    @Override
    public boolean includeHelpCommand() {
        return true;
    }

    @Override
    public boolean includeReloadCommand() {
        return true;
    }

}
