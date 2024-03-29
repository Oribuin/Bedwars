package in.oribu.bedwars.database.migration;

import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.database.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class _1_CreateInitialTables extends DataMigration {

    public _1_CreateInitialTables() {
        super(1);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) throws SQLException {
        String globalData = "CREATE TABLE IF NOT EXISTS " + tablePrefix + "global (" +
                            "`uuid` VARCHAR(36) NOT NULL PRIMARY KEY, " +
                            "`name` VARCHAR(36) NOT NULL, " +
                            "`data` TEXT NOT NULL" +
                            ");";

        try (PreparedStatement statement = connection.prepareStatement(globalData)) {
            statement.executeUpdate();
        }

    }

}
