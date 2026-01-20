package com.wdiscute.mooncatcher.datapack;

import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.util.BsonUtil;
import it.unimi.dsi.fastutil.Pair;
import org.bson.BsonDocument;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;

public class DatapackReader<T extends DatapackEntry> {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final String packName;
    private final Path packDirectory;
    private final Map<String, T> datapackObjects = new HashMap<>();
    private final BuilderCodec<T> codec;
    private DatapackProvider<T> provider = null;

    /**
     * Reads a datapack with a backup provider so if no entries are read then they can be created
     * @param dataDirectory The plugins data directory
     * @param packName The name of the datapack
     * @param codec Codec for encoding/decoding the datapack
     * @param provider backup datapack provider, null for no backup
     */
    public DatapackReader(Path dataDirectory, String packName, BuilderCodec<T> codec, @Nullable DatapackProvider<T> provider) {
        this.packDirectory = Path.of(dataDirectory.toString(), "/" + packName + "/");
        this.packName = packName;
        this.codec = codec;
        this.provider = provider;
        load(packDirectory.toFile());

        if (datapackObjects.isEmpty() && this.provider != null) {
            LOGGER.atInfo().log("No entries found in %s. Generating new entries");
            saveData();
        }
        saveToManager(datapackObjects.values().toArray(new DatapackEntry[]{}));
        LOGGER.at(Level.INFO).log("Loaded %s with %d entries", packName, datapackObjects.size());
    }

    /**
     * Reads a datapack without a backup provider
     * @param dataDirectory The plugins data directory
     * @param packName The name of the datapack
     * @param codec Codec for encoding/decoding the datapack
     */
    public DatapackReader(Path dataDirectory, String packName, BuilderCodec<T> codec) {
        this(dataDirectory, packName, codec, null);
    }

    /**
     * Reads the datapack from a given pack directory
     * @param packFolder
     */
    private void load(File packFolder) {
        if (packFolder == null || !packFolder.isDirectory()) return;
        for (File file : packFolder.listFiles()) {
            if (file.isDirectory()) load(file);
            else readData(file);
        }
    }

    /**
     * Saves the entries to the datapack manager. Override for customisation in how they are saved such as sorting.
     * @param values The entries unsorted.
     */
    public void saveToManager(DatapackEntry[] values) {
        DatapackManager.saveEntries(this.packName, values);
    }

    /**
     * Reads a given datapack file
     * @param file the file to convert into a entry object
     */
    private void readData(File file) {
        try {
            BsonDocument document = BsonUtil.readDocumentNow(file.toPath());
            if (document != null) {
                ExtraInfo extraInfo = ExtraInfo.THREAD_LOCAL.get();
                this.datapackObjects.put(file.getName(), this.codec.decode(document, extraInfo));
                extraInfo.getValidationResults().logOrThrowValidatorExceptions(LOGGER);
            }
        } catch (Exception var3) {
            LOGGER.at(Level.WARNING).withCause(var3).log("Failed to load %s", file.toString());
        }
    }

    /**
     * Saves data using the default provider
     */
    private void saveData() {
        if (this.packDirectory != null) {
            try {
                if (!Files.exists(this.packDirectory)) {
                    Files.createDirectories(packDirectory);
                }

                List<Pair<String, T>> data = this.provider.generatePack(new ArrayList<>());
                for (Pair<String, T> entry : data) {
                    this.datapackObjects.put(entry.first(), entry.second());
                    Path file = this.packDirectory.resolve(entry.first());
                    BsonUtil.writeSync(file, this.codec, entry.second(), LOGGER);
                }

            } catch (IOException var1) {
                LOGGER.at(Level.WARNING).withCause(var1).log("Failed to %s", this.packName);
            }
        }
    }

    /**
     * Gets a map of file name > data pack entry
     * @return Map of read entries and their names
     */
    public Map<String, T> getDataObjects() {
        return datapackObjects;
    }

    /**
     * Gets the collection of the read entries
     * @return data pack entries unsorted
     */
    public Collection<T> getDataValues() {
        return datapackObjects.values();
    }
}
