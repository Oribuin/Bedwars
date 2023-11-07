package in.oribu.bedwars.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.rosegarden.utils.HexUtils;
import in.oribu.bedwars.manager.ConfigurationManager.Setting;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.concurrent.atomic.AtomicInteger;

public class ScoreboardManager extends Manager implements Listener {

    private Scoreboard scoreboard = null; // The scoreboard
    private Objective objective = null; // The objective
    private BukkitTask updateTask = null; // The update task

    public ScoreboardManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        final org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        this.scoreboard = manager.getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("bedwars", Criteria.DUMMY, HexUtils.colorify("<r:0.7>Oribuin Bedwars"));
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        final AtomicInteger slot = new AtomicInteger(-1);
        Setting.SCOREBOARD_LINES.getStringList().forEach((line) -> {
            final org.bukkit.scoreboard.Team team = this.scoreboard.registerNewTeam("bedwars-" + slot.incrementAndGet());
            final String entry = String.format("[%d] ", slot.get());

            team.addEntry(entry);
            final Score score = this.objective.getScore(entry);
            score.setScore(0);
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

        String title = HexUtils.colorify("&#4097f5&lBedwars");
        title = title.substring(0, Math.min(128, title.length()));

        this.objective.setDisplayName(title);

        final AtomicInteger slot = new AtomicInteger(-1);
        Setting.SCOREBOARD_LINES.getStringList().forEach((line) -> {
            final org.bukkit.scoreboard.Team team = this.scoreboard.getTeam("bedwars-" + slot.incrementAndGet());
            if (team == null) return;

            team.setSuffix(HexUtils.colorify(line));
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
