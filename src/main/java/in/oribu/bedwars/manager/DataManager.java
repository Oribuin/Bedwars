package in.oribu.bedwars.manager;

import com.google.gson.Gson;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.manager.AbstractDataManager;
import in.oribu.bedwars.database.migration._1_CreateInitialTables;
import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.MatchPlayer;
import in.oribu.bedwars.storage.Stats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataManager extends AbstractDataManager {

    private static final Gson GSON = new Gson();
    private final Map<UUID, Stats> userCache = new HashMap<>();

    public DataManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    /**
     * Load a user's global stats from the database.
     *
     * @param player The player to load
     */
    public void load(Player player) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "global WHERE uuid = ?")) {
                statement.setString(1, String.valueOf(player.getUniqueId()));

                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    this.userCache.put(player.getUniqueId(), GSON.fromJson(result.getString("data"), Stats.class));
                }
            }
        }));
    }

    /**
     * Save every player from a match.
     *
     * @param match The match
     */
    public void savePlayers(Match match) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String query = "REPLACE INTO " + this.getTablePrefix() + "global (uuid, `name`, `data`) VALUES (?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {

                // Save every player within the match.
                for (MatchPlayer player : match.getPlayers()) {
                    Stats stats = this.userCache.getOrDefault(
                            player.getUUID(),
                            new Stats()
                    );

                    stats.add(player);

                    statement.setString(1, player.getUUID().toString());
                    statement.setString(2, player.getName());
                    statement.setString(3, GSON.toJson(stats));
                    statement.addBatch();
                    this.userCache.put(player.getUUID(), stats);
                }

                statement.executeBatch();
            }
        }));
    }

    /**
     * Execute a runnable async off the main thread
     *
     * @param runnable The runnable to run async
     */
    public void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this.rosePlugin, runnable);
    }


    @Override
    public List<Class<? extends DataMigration>> getDataMigrations() {
        return List.of(_1_CreateInitialTables.class);
    }

}
