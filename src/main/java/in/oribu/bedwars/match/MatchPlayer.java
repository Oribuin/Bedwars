package in.oribu.bedwars.match;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class MatchPlayer {

    private final UUID uuid; // The player's UUID
    private final String name; // The player's name
    private final String team; // The player's team
    private boolean dead; // If the player is deadge
    private int kill; // The player's kills
    private int deaths; // The player's deaths
    private int finals; // The player's final kills

    public MatchPlayer(UUID uuid, String team) {
        this.uuid = uuid;
        this.name = this.getPlayer().getName();
        this.team = team;
        this.dead = false;
    }

    /**
     * Eliminate the user from the match.
     *
     * @param match The match to eliminate the player from
     */
    public void eliminate(Match match) {
        this.dead = true;
        this.deaths++;

        // TODO: Check how many members of the team are alive
        // TODO: Teleport the player to the map center.
        // TODO: Play elimination sound, message and finisher
        // TODO: Drop ender chest contents into team generator.

        Player player = this.getPlayer();

        // Make the player a spectator
        player.setGameMode(GameMode.CREATIVE);
        player.setAllowFlight(true);
        player.getInventory().clear();
        player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 0));
//        player.teleport(match.getMap().getCenter()); // TODO: Use TeleportAsync where possible

        // TODO: Hide the player from other players

    }

    /**
     * Get the player's name from the UUID
     *
     * @return The player's name
     */
    public Player getPlayer() {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            throw new IllegalArgumentException("Player with UUID " + uuid + " is not online");
        }

        return player;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getTeam() {
        return this.team;
    }

    public boolean isDead() {
        return this.dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getKill() {
        return this.kill;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getFinals() {
        return this.finals;
    }

    public void setFinals(int finals) {
        this.finals = finals;
    }
}
