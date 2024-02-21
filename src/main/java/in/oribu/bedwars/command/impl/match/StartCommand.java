package in.oribu.bedwars.command.impl.match;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import in.oribu.bedwars.manager.GameManager;
import in.oribu.bedwars.match.Match;
import org.bukkit.entity.Player;

public class StartCommand extends BaseRoseCommand {

    public StartCommand(RosePlugin rosePlugin) {
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


        match.start();
        context.getSender().sendMessage("Started match");
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("start")
                .permission("bedwars.start")
                .build();
    }

}
