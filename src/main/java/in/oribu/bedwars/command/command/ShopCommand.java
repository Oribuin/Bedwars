package in.oribu.bedwars.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import in.oribu.bedwars.shop.Shop;
import in.oribu.bedwars.shop.ShopItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class ShopCommand extends RoseCommand {

    public ShopCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, Shop shop) {
        Player player = (Player) context.getSender();
        PaginatedGui gui = shop.create();

        for (ShopItem item : shop.getItems().values()) {
            gui.addItem(new GuiItem(item.getItem(), event -> item.buy(player)));
        }
        gui.open((HumanEntity) context.getSender());
    }

    @Override
    protected String getDefaultName() {
        return "shop";
    }

    @Override
    public String getDescriptionKey() {
        return "command-shop-description";
    }

    @Override
    public String getRequiredPermission() {
        return "bedwars.shop";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

}
