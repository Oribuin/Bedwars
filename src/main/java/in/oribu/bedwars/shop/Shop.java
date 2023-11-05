package in.oribu.bedwars.shop;

import java.util.Map;

public class Shop {

    private final String name;
    private final Map<Integer, ShopItem> items;
    private final int size;

    public Shop(String name, Map<Integer, ShopItem> items, int size) {
        this.name = name;
        this.items = items;
        this.size = size;
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

}
