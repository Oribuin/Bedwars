package in.oribu.bedwars.command.impl;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import in.oribu.bedwars.command.impl.level.CreateCommand;
import in.oribu.bedwars.command.impl.level.GeneratorCommand;

public class LevelCommand extends BaseRoseCommand {

    public LevelCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("level")
                .descriptionKey("command-level-description")
                .permission("bedwars.level")
                .build();
    }

    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .requiredSub(new CreateCommand(this.rosePlugin), new GeneratorCommand(this.rosePlugin));
    }
}
