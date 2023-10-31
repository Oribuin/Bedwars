package in.oribu.bedwars.match;

import in.oribu.bedwars.match.upgrade.UpgradeType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Team {

    private final String name;
    private final Location spawn;
    private final ChatColor teamColor;
    private Map<UUID, MatchPlayer> players;
    private final Map<UpgradeType, Integer> upgrades;
    private boolean hasBed;
    private boolean eliminated;

    /**
     * The constructor for the team
     *
     * @param name      The name of the team
     * @param spawn     The spawn location of the team
     * @param teamColor The color of the team
     */
    public Team(String name, Location spawn, ChatColor teamColor) {
        this.name = name;
        this.spawn = spawn;
        this.teamColor = teamColor;
        this.players = new HashMap<>();
        this.upgrades = new HashMap<>();
        this.hasBed = true;
        this.eliminated = false;
    }

    /**
     * Tick all the island upgrades and such
     */
    public void tick(Match match) {
        this.upgrades.forEach((type, level) -> {
            if (level == 0) return;

            type.getUpgrade().tick(match, this, level);
        });
    }

    /**
     * Upgrade a team to the upgrade type
     *
     * @param type The type of upgrade
     */
    public void upgrade(Match match, UpgradeType type) {
        // TODO: Add check to see if the team can upgrade
        // TODO: Sent message to the team that they have upgraded

        final int newLevel = this.upgrades.getOrDefault(type, 0) + 1;
        this.upgrades.put(type, newLevel);

        type.getUpgrade().equip(this, newLevel);
    }

    public String getName() {
        return this.name;
    }

    public Location getSpawn() {
        return this.spawn;
    }

    public ChatColor getTeamColor() {
        return this.teamColor;
    }

    public Map<UUID, MatchPlayer> getPlayers() {
        return this.players;
    }

    public void setPlayers(Map<UUID, MatchPlayer> players) {
        this.players = players;
    }

    public Map<UpgradeType, Integer> getUpgrades() {
        return this.upgrades;
    }

    public boolean isHasBed() {
        return this.hasBed;
    }

    public void setHasBed(boolean hasBed) {
        this.hasBed = hasBed;
    }

    public boolean isEliminated() {
        return this.eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

}
