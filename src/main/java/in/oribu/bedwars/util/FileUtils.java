package in.oribu.bedwars.util;

import dev.rosewood.rosegarden.RosePlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtils {

    /**
     * Create a file in a folder from the plugin's resources
     *
     * @param rosePlugin The plugin
     * @param folders    The folders
     * @return The file
     */
    @NotNull
    @SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedReturnValue"})
    public static File createFile(@NotNull RosePlugin rosePlugin, @NotNull String... folders) {
        File file = new File(rosePlugin.getDataFolder(), String.join("/", folders)); // Create the file
        if (file.exists())
            return file;

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        String path = String.join("/", folders);
        try (InputStream stream = rosePlugin.getResource(path)) {
            if (stream == null) {
                file.createNewFile();
                return file;
            }

            Files.copy(stream, Paths.get(file.getAbsolutePath()));
        } catch (IOException ignored) {
        }

        return file;
    }

}
