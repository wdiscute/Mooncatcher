package com.wdiscute.starcatcher;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class StarcatcherConfig
{
    public static final BuilderCodec<StarcatcherConfig> CODEC = BuilderCodec.builder(StarcatcherConfig.class, StarcatcherConfig::new)
            .append(new KeyedCodec<>(
                            "MinFishingTime", Codec.INTEGER), (config, value) ->
                            config.justAnInt = value,
                    (config) -> config.justAnInt)
            .documentation("this is an int :)").add()
            .build();

    public int justAnInt = 67;
}
