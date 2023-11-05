package in.oribu.bedwars.match;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import in.oribu.bedwars.BedwarsPlugin;
import in.oribu.bedwars.match.generator.Generator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Level {

    private final @NotNull String name; // The name of the map
    private final @NotNull Location center; // The center of the map
    private final @NotNull List<Generator> generators; // The generators in the map
    private final @NotNull File file; // The file of the map
    private final @Nullable ClipboardFormat format; // The clipboard format.
    private int islandRadius; // The radius of the island

    public Level(@NotNull String name, @NotNull Location center, @NotNull String fileName) {
        this.name = name;
        this.center = center;
        this.generators = new ArrayList<>();
        this.file = new File(BedwarsPlugin.get().getDataFolder(), "schematics/" + fileName);
        this.format = ClipboardFormats.findByFile(this.file);
        this.islandRadius = 25;
    }

    /**
     * Load the map into the world
     */
    public void load() {
        // Load the map schematic into the world.
        final PluginManager manager = Bukkit.getPluginManager();
        if (manager.isPluginEnabled("FastAsyncWorldEdit") || manager.isPluginEnabled("AsyncWorldEdit")) {
            Bukkit.getScheduler().runTaskAsynchronously(BedwarsPlugin.get(), () -> this.paste());
        } else {
            this.paste();
        }

        // Create the island generators in their designated locations.
        this.generators.forEach(Generator::create);
    }

    /**
     * Paste the map schematic into the world
     */
    private void paste() {
        if (this.format == null || center.getWorld() == null) {
            throw new IllegalStateException("Invalid schematic provided for map: " + this.name);
        }

        // Create the clipboard schematic
        Clipboard clipboard = null;
        try (FileInputStream stream = new FileInputStream(this.file); ClipboardReader reader = this.format.getReader(stream)) {
            clipboard = reader.read();
        } catch (IOException ignored) {
        }

        if (clipboard == null) {
            throw new IllegalStateException("Unable to read schematic for map: " + this.name);
        }

        final Clipboard finalClipboard = clipboard;
        try (final EditSession session = WorldEdit.getInstance().newEditSessionBuilder()
                .world(BukkitAdapter.adapt(this.center.getWorld()))
                .maxBlocks(-1)
                .build()
        ) {
            Operations.complete(new ClipboardHolder(finalClipboard).createPaste(session)
                    .to(BukkitAdapter.asBlockVector(this.center))
                    .copyEntities(false)
                    .copyBiomes(false)
                    .ignoreAirBlocks(true)
                    .build()
            );
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull Location getCenter() {
        return this.center;
    }

    public @NotNull List<Generator> getGenerators() {
        return this.generators;
    }

    public int getIslandRadius() {
        return this.islandRadius;
    }

    public void setIslandRadius(int islandRadius) {
        this.islandRadius = islandRadius;
    }

}
