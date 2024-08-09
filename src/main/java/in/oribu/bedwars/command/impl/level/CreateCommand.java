package in.oribu.bedwars.command.impl.level;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import in.oribu.bedwars.manager.GameManager;
import in.oribu.bedwars.match.Level;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CreateCommand extends BaseRoseCommand {

    public CreateCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        Player sender = (Player) context.getSender();
        String name = context.get("name");

        File folder = new File(this.rosePlugin.getDataFolder(), "levels");
        File file = new File(folder, name + ".yml");

        if (!folder.exists()) folder.mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }

        Level level = new Level(name, sender.getLocation().toCenterLocation(), file);

        level.save();

        this.rosePlugin.getManager(GameManager.class).cache(level);
        sender.sendMessage(Component.text("Created level " + name + " at your location!"));
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("create")
                .permission("bedwars.level.create")
                .playerOnly(true)
                .arguments(this.createArgumentsDefinition())
                .build();
    }

    private ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .required("name", ArgumentHandlers.STRING)
                .build();
    }

}
