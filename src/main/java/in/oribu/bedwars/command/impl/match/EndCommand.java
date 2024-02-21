package in.oribu.bedwars.command.impl.match;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import in.oribu.bedwars.manager.GameManager;
import in.oribu.bedwars.match.Match;

public class EndCommand extends BaseRoseCommand {

    public EndCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        GameManager gameManager = this.rosePlugin.getManager(GameManager.class);
        Match match = gameManager.getActiveMatch();
        if (match == null) {
            context.getSender().sendMessage("No active match found");
            return;
        }


        match.end();
        context.getSender().sendMessage("Ended match");
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("end")
                .permission("bedwars.end")
                .build();
    }

}
