package com.wdiscute.mooncatcher.datapack;

import com.hypixel.hytale.codec.builder.BuilderCodec;

public interface DatapackEntry {
    BuilderCodec<?> codec();
}
