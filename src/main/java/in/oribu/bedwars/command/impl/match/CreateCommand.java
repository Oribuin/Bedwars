package in.oribu.bedwars.command.impl.match;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import in.oribu.bedwars.command.argument.LevelArgumentHandler;
import in.oribu.bedwars.manager.GameManager;
import in.oribu.bedwars.match.Level;

public class CreateCommand extends BaseRoseCommand {

    public CreateCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        GameManager gameManager = this.rosePlugin.getManager(GameManager.class);
        Level level = context.get("level");

        gameManager.createNewMatch(level);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("create")
                .permission("bedwars.start")
                .arguments(this.createArgumentsDefinition())
                .build();
    }


    public ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .required("level", new LevelArgumentHandler())
                .build();
    }

}
