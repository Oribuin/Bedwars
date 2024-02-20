package in.oribu.bedwars.listener;

import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.manager.GameManager;
import in.oribu.bedwars.match.Match;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class MatchListener implements Listener {

    private final BedwarsPlugin plugin;

    public MatchListener(BedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event) {
        Match match = this.plugin.getManager(GameManager.class).getActiveMatch();
        if (match == null) return;

        if (match.canBeBroken(event.getBlock()))
            return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onExplode(EntityExplodeEvent event) {
        Match match = this.plugin.getManager(GameManager.class).getActiveMatch();
        if (match == null) return;

        event.blockList().removeIf(block -> !match.canBeBroken(block));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockExplode(BlockExplodeEvent event) {
        Match match = this.plugin.getManager(GameManager.class).getActiveMatch();
        if (match == null) return;

        event.blockList().removeIf(block -> !match.canBeBroken(block));
    }

}
