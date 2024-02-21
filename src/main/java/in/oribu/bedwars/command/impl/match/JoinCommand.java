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
import in.oribu.bedwars.match.Match;
import org.bukkit.entity.Player;

public class JoinCommand extends BaseRoseCommand {

    public JoinCommand(RosePlugin rosePlugin) {
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

        match.join((Player) context.getSender());
        context.getSender().sendMessage("Joined match");
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("join")
                .permission("bedwars.join")
                .build();
    }

}
