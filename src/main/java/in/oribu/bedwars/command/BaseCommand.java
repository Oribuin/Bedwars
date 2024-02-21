package in.oribu.bedwars.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import in.oribu.bedwars.command.impl.ItemCommand;
import in.oribu.bedwars.command.impl.LevelCommand;
import in.oribu.bedwars.command.impl.MatchCommand;
import in.oribu.bedwars.command.impl.ShopCommand;

public class BaseCommand extends BaseRoseCommand {

    public BaseCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("bedwars")
                .descriptionKey("command-base-description")
                .aliases("bw")
                .permission("bedwars.use")
                .build();
    }

    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .requiredSub(
                        new ItemCommand(this.rosePlugin),
                        new LevelCommand(this.rosePlugin),
                        new MatchCommand(this.rosePlugin),
                        new ShopCommand(this.rosePlugin)
                );
    }

}
