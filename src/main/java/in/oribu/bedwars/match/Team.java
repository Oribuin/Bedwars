package in.oribu.bedwars.match;

import in.oribu.bedwars.match.generator.Generator;
import in.oribu.bedwars.upgrade.UpgradeType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Team {

    private final @NotNull String name;
    private final @NotNull Location spawn;
    private final @NotNull ChatColor teamColor;
    private @NotNull Generator generator;
    private @NotNull Map<UUID, MatchPlayer> players;
    private final @NotNull Map<UpgradeType, Integer> upgrades;
    private boolean hasBed;
    private boolean eliminated;
    private int maxPlayers;

    /**
     * The constructor for the team
     *
     * @param name      The name of the team
     * @param spawn     The spawn location of the team
     * @param generator The generator of the team
     * @param teamColor The color of the team
     */
    public Team(@NotNull String name, @NotNull Location spawn, @NotNull Generator generator, @NotNull ChatColor teamColor) {
        this.name = name;
        this.spawn = spawn;
        this.generator = generator;
        this.teamColor = teamColor;
        this.players = new HashMap<>();
        this.upgrades = new HashMap<>();
        this.hasBed = true;
        this.eliminated = false;
        this.maxPlayers = 1;
    }

    /**
     * Tick all the island upgrades and such
     */
    public void tick(Match match) {
        this.upgrades.forEach((type, level) -> {
            if (level == 0) return;

            type.getUpgrade().tick(match, this, level);
        });

        this.generator.tick();
    }

    /**
     * Add a player into the team
     *
     * @param player The player to join the team
     */
    public void join(Player player) {
        if (this.players.size() >= this.maxPlayers) {
            Bukkit.getLogger().warning("Player " + player.getName() + " tried to join team " + this.name + " but it was full!");
            return;
        }

        // TODO: Check if the player is already in a team

        this.players.put(player.getUniqueId(), new MatchPlayer(player, this.name));
        player.sendMessage("You have joined team " + this.name + "!");
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

        type.getUpgrade().equip(match, this, newLevel);
        this.getPlayers().values().stream().map(MatchPlayer::getPlayer)
                .forEach(player -> player.sendMessage("Your team has upgraded " + type.name() + " to level " + newLevel + "!"));
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull Location getSpawn() {
        return this.spawn;
    }

    public @NotNull Generator getGenerator() {
        return this.generator;
    }

    public void setGenerator(@NotNull Generator generator) {
        this.generator = generator;
    }

    public @NotNull ChatColor getTeamColor() {
        return this.teamColor;
    }

    public @NotNull Map<UUID, MatchPlayer> getPlayers() {
        return this.players;
    }

    public void setPlayers(@NotNull Map<UUID, MatchPlayer> players) {
        this.players = players;
    }

    public @NotNull Map<UpgradeType, Integer> getUpgrades() {
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

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

}
