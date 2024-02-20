package in.oribu.bedwars.command.impl;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import in.oribu.bedwars.command.argument.CustomItemArgumentHandler;
import in.oribu.bedwars.item.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand extends BaseRoseCommand {

    public ItemCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context, CustomItem item) {
        if (!(context.getSender() instanceof Player player))
            return;

        ItemStack itemStack = item.getItem();
        player.getInventory().addItem(itemStack);
        player.sendMessage("You have been given the item " + item.getName());
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("item")
                .descriptionKey("command-item-description")
                .permission("bedwars.item")
                .playerOnly(true)
                .build();
    }


    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .required("item", new CustomItemArgumentHandler())
                .build();
    }
}
