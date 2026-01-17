package com.wdiscute.mooncatcher;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class MooncatcherConfig
{
    public static final BuilderCodec<MooncatcherConfig> CODEC = BuilderCodec.builder(MooncatcherConfig.class, MooncatcherConfig::new)
            .append(new KeyedCodec<>(
                            "MinFishingTime", Codec.INTEGER), (config, value) ->
                            config.justAnInt = value,
                    (config) -> config.justAnInt)
            .documentation("this is an int :)").add()
            .build();

    public int justAnInt = 67;
}
