package in.oribu.bedwars.item;

import in.oribu.bedwars.upgrade.ContextHandler;

public abstract class CustomItem {

    protected final String name;

    protected CustomItem(String name) {
        this.name = name;
    }

    /**
     * The event called with the item
     *
     * @param handler The context handler
     */
    public abstract void event(ContextHandler handler);

}
