package in.oribu.bedwars.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import in.oribu.bedwars.item.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand extends RoseCommand {

    public ItemCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
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
    protected String getDefaultName() {
        return "item";
    }

    @Override
    public String getDescriptionKey() {
        return "command-item-description";
    }

    @Override
    public String getRequiredPermission() {
        return "bedwars.item";
    }

}
