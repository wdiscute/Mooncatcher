package com.wdiscute.mooncatcher.datapack;

import it.unimi.dsi.fastutil.Pair;

import java.util.List;

public abstract class DatapackProvider<T extends DatapackEntry> {

    /**
     * Called when no datapack contents are found to generate them from scratch
     * @param packContents The pack contents made so far. New entries should be added to this. Pairs are < Entry name, Entry >
     * @return Complete list of the pack entries
     */
    public abstract List<Pair<String, T>> generatePack(List<Pair<String, T>> packContents);

    /**
     * Adds an entry to a given list
     * @param entry The entry to add
     * @param entryName The name of the entry
     * @param contents The entry list
     * @return the entry list
     */
    public List<Pair<String, T>> add(T entry, String entryName, List<Pair<String, T>> contents) {
        if (!entryName.endsWith(".json")) entryName += ".json";
        contents.add(Pair.of(entryName, entry));
        return contents;
    }
}
