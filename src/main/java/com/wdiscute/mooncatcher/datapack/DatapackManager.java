package com.wdiscute.mooncatcher.datapack;

import java.util.HashMap;
import java.util.Map;

public class DatapackManager {
    private static final Map<String, DatapackEntry[]> ENTRIES = new HashMap<>();

    /**
     * Saves the entries to the manager
     * @param datapack The name of the datapack
     * @param entries The entries to save
     */
    protected static void saveEntries(String datapack, DatapackEntry[] entries) {
        ENTRIES.put(datapack, entries);
    }

    /**
     * Gets all of a datapacks entries
     * @param datapack The datapack to get entries from
     * @return The datapack entries
     */
    public static DatapackEntry[] getDatapack(String datapack) {
        return ENTRIES.get(datapack);
    }
}
