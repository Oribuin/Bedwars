package in.oribu.bedwars.listener;

import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.manager.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListeners implements Listener {

    private final ScoreboardManager manager;

    public PlayerListeners(BedwarsPlugin plugin) {
        this.manager = plugin.getManager(ScoreboardManager.class);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (this.manager.getScoreboard() == null) return;

        event.getPlayer().setScoreboard(this.manager.getScoreboard());
    }

}
