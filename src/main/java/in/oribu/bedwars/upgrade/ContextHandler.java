package in.oribu.bedwars.upgrade;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ContextHandler(@NotNull Event event, @Nullable ItemStack itemStack, @Nullable Player player) {

    /**
     * Cast the event to the specified class
     *
     * @param clazz The class to cast to
     * @param <T>   The type of the event
     * @return The casted event
     */
    public <T extends Event> T as(Class<T> clazz) {
        if (!clazz.isInstance(this.event))
            return null;

        return clazz.cast(this.event);
    }

}
