package in.oribu.bedwars.shop;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class Shop {

    private final String name;
    private final Map<Integer, ShopItem> items;
    private final int size;
    private String title; // GUI Title

    public Shop(String name, Map<Integer, ShopItem> items, int size) {
        this.name = name;
        this.items = items;
        this.size = size;
        this.title = StringUtils.capitalize(name);
    }

    /**
     * Create a PaginatedGui from the shop
     *
     * @return The PaginatedGui
     */
    public PaginatedGui create() {
        return Gui.paginated()
                .title(Component.text(this.title))
                .rows(this.size / 9)
                .disableAllInteractions()
                .create();
    }

    public String getName() {
        return name;
    }

    public Map<Integer, ShopItem> getItems() {
        return items;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
