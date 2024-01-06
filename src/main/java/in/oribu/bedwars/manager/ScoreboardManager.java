package in.oribu.bedwars.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.rosegarden.utils.HexUtils;
import in.oribu.bedwars.manager.ConfigurationManager.Setting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.concurrent.atomic.AtomicInteger;

public class ScoreboardManager extends Manager implements Listener {

    private Scoreboard scoreboard = null; // The scoreboard
    private Objective objective = null; // The objective
    private BukkitTask updateTask = null; // The update task

    public ScoreboardManager(RosePlugin rosePlugin) {
        super(rosePlugin);

        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        this.scoreboard = manager.getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("bedwars", Criteria.DUMMY, HexUtils.colorify("<r:0.7>Oribuin Bedwars"));
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    @Override
    public void reload() {
        this.scoreboard.getTeams().forEach(org.bukkit.scoreboard.Team::unregister);
        AtomicInteger slot = new AtomicInteger(-1);
        Setting.SCOREBOARD_LINES.getStringList().forEach((line) -> {
            org.bukkit.scoreboard.Team team = this.scoreboard.registerNewTeam("bedwars-" + slot.incrementAndGet());
            String entry = ChatColor.values()[slot.get()].toString() + ChatColor.RESET;

            team.addEntry(entry);
            this.objective.getScore(entry).setScore(15 - slot.get());
        });

        this.updateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this.rosePlugin, this::update, 0L, 2);
    }

    /**
     * Update the scoreboard
     */
    public void update() {
        if (this.scoreboard == null) return;
        if (this.objective == null) return;
        if (Bukkit.getOnlinePlayers().isEmpty()) return;

        String title = HexUtils.colorify(PlaceholderAPIHook.applyPlaceholders(null, Setting.SCOREBOARD_TITLE.getString()));
        this.objective.setDisplayName(title.substring(0, Math.min(title.length(), 128)));

        AtomicInteger slot = new AtomicInteger(-1);
        Setting.SCOREBOARD_LINES.getStringList().forEach((line) -> {
            org.bukkit.scoreboard.Team team = this.scoreboard.getTeam("bedwars-" + slot.incrementAndGet());
            if (team == null) return;

            team.setSuffix(HexUtils.colorify(PlaceholderAPIHook.applyPlaceholders(null, line)));
        });
    }

    @Override
    public void disable() {
        // TODO: Remove scoreboard
        if (this.updateTask != null) {
            this.updateTask.cancel();
        }

    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

}
