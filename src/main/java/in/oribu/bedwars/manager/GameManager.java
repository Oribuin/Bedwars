package in.oribu.bedwars.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import in.oribu.bedwars.match.Level;
import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.MatchStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

public class GameManager extends Manager {

    private @Nullable Match activeMatch;
    private BukkitTask countdownTask; // The task that makes sure theres enough players to start the match

    public GameManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {

    }

    /**
     * Create a new match from the level and load it
     *
     * @param level The level to create the match from
     */
    public void createNewMatch(Level level) {
        if (this.activeMatch != null) {
            Bukkit.getLogger().severe("Tried to create a new match while another match is active!");
            return;
        }

        // Wait for the level to load before creating the match and setting it as the active match
        level.load().thenAccept(unused -> {
            this.activeMatch = new Match(level);
            this.activeMatch.setStatus(MatchStatus.WAITING);

            // Tell all the players that they can join the match when its ready
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Component.text("A new match has been created! Type /bw join to join the match!")));
        });
    }

    @Override
    public void disable() {
        if (this.countdownTask != null) {
            this.countdownTask.cancel();
            this.countdownTask = null;
        }
    }

    @Nullable
    public Match getActiveMatch() {
        return this.activeMatch;
    }

}
