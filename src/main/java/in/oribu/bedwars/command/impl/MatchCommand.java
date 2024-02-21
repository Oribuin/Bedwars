package in.oribu.bedwars.command.impl;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import in.oribu.bedwars.command.impl.match.CreateCommand;
import in.oribu.bedwars.command.impl.match.EndCommand;
import in.oribu.bedwars.command.impl.match.JoinCommand;
import in.oribu.bedwars.command.impl.match.StartCommand;

public class MatchCommand extends BaseRoseCommand {

    public MatchCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("match")
                .descriptionKey("command-match-description")
                .permission("bedwars.match")
                .build();
    }

    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .requiredSub(
                        new CreateCommand(this.rosePlugin),
                        new EndCommand(this.rosePlugin),
                        new JoinCommand(this.rosePlugin),
                        new StartCommand(this.rosePlugin)
                );
    }
}
