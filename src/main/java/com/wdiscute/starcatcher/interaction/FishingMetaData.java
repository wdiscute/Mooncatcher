package com.wdiscute.starcatcher.interaction;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import java.util.UUID;

public class FishingMetaData
{
    public static final String KEY = "StarcatcherFishingUUID";
    public static final BuilderCodec<FishingMetaData> CODEC = BuilderCodec.builder(FishingMetaData.class, FishingMetaData::new)
            .append(new KeyedCodec<>(
                            "FishingUUID", Codec.UUID_BINARY), (metaData, value) ->
                            metaData.fishingUUID = value,
                    (config) -> config.fishingUUID)
            .documentation("The bobber that is bound to the fishing rod").add()
            .build();
    public static final KeyedCodec<FishingMetaData> KEYED_CODEC = new KeyedCodec<>(KEY, CODEC);
    private UUID fishingUUID = null;

    public void setFishingUUID(UUID uuid)
    {
        this.fishingUUID = uuid;
    }

    public UUID getFishingUUID()
    {
        return this.fishingUUID;
    }
}
