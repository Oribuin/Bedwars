package in.oribu.bedwars.command.impl.level;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import in.oribu.bedwars.command.argument.LevelArgumentHandler;
import in.oribu.bedwars.match.Level;
import in.oribu.bedwars.match.generator.Generator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class GeneratorCommand extends BaseRoseCommand {

    public GeneratorCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        Player sender = (Player) context.getSender();
        Level level = context.get("level");
        int delay = context.get("delay");

        Map<Material, Integer> items = new HashMap<>(Map.of(Material.DIAMOND, 1));

        Generator generator = new Generator(items, sender.getLocation().toCenterLocation());
        generator.setHologramIcon(Material.DIAMOND_BLOCK);
        level.getGenerators().add(generator);
        sender.sendMessage("Created generator at your location!");

        generator.create();
        level.save();

        sender.sendMessage("Ticking generator for 10 seconds... then destroying");

        BukkitTask tickTask = Bukkit.getScheduler().runTaskTimer(this.rosePlugin, generator::tick, 0L, 1);

        sender.getServer().getScheduler().runTaskLater(this.rosePlugin, () -> {
            generator.destroy();
            tickTask.cancel();
        }, delay * 20L);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("generator")
                .permission("bedwars.level.generator")
//                .playerOnly(true)
                .build();
    }

    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .required("level", new LevelArgumentHandler())
                .required("delay", ArgumentHandlers.INTEGER)
                .build();
    }

}
