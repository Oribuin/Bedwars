package in.oribu.bedwars.shop;

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
