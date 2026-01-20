package com.wdiscute.mooncatcher;

import com.hypixel.hytale.common.util.TimeUtil;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.asset.type.environment.config.Environment;
import com.hypixel.hytale.server.core.asset.type.weather.config.Weather;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import java.util.Random;

public class U
{
    public static Random r = new Random();

    public static String getEnvName(World world, Vector3d blockPos)
    {
        Store<ChunkStore> chunkStore = world.getChunkStore().getStore();
        return getEnvName(chunkStore, blockPos);
    }

    public static String getEnvName(Store<ChunkStore> chunkStore, Vector3d blockPos)
    {
        long chunkIndex = ChunkUtil.indexChunkFromBlock(blockPos.x, blockPos.z);
        Ref<ChunkStore> chunkRef = chunkStore.getExternalData().getChunkReference(chunkIndex);
        BlockChunk blockChunkComp = chunkStore.getComponent(chunkRef, BlockChunk.getComponentType());
        return getEnvName(blockChunkComp, blockPos);
    }

    public static String getEnvName(BlockChunk blockChunkComp, Vector3d blockPos)
    {
        int envId = blockChunkComp.getEnvironment(blockPos);
        Environment environment = Environment.getAssetMap().getAsset(envId);
        if (environment == null) return "unknown";
        return environment.getId();
    }

    public static String getWeatherName(World world, Vector3d blockPos)
    {
        Store<ChunkStore> chunkStore = world.getChunkStore().getStore();
        return getWeatherName(chunkStore, blockPos);
    }

    public static String getWeatherName(Store<ChunkStore> chunkStore, Vector3d blockPos)
    {
        long chunkIndex = ChunkUtil.indexChunkFromBlock(blockPos.x, blockPos.z);
        Ref<ChunkStore> chunkRef = chunkStore.getExternalData().getChunkReference(chunkIndex);
        BlockChunk blockChunkComp = chunkStore.getComponent(chunkRef, BlockChunk.getComponentType());
        return getWeatherName(blockChunkComp, blockPos);
    }

    public static String getWeatherName(BlockChunk blockChunkComp, Vector3d blockPos)
    {
        int envId = blockChunkComp.getEnvironment(blockPos);
        Weather weather = Weather.getAssetMap().getAsset(envId);
        if (weather == null) return "unknown";
        return weather.getId();
    }

    public static float getDaytimePercentage(World world, Vector3d blockPos)
    {
        WorldTimeResource time = world.getEntityStore().getStore().getResource(WorldTimeResource.getResourceType());
        return time.getDayProgress();
    }


    public static Vector3d offsetVectorByRandom(Vector3d v, int x, int y, int z)
    {
        return offsetVectorByRandom(v, x, y, (double) z);
    }

    public static Vector3d offsetVectorByRandom(Vector3d v, float x, float y, float z)
    {
        return offsetVectorByRandom(v, x, y, (double) z);
    }

    public static Vector3d offsetVectorByRandom(Vector3d v, double x, double y, double z)
    {
        double x2 = x == 0 ? 0 : U.r.nextDouble(x) - x / 2;
        double y2 = y == 0 ? 0 : U.r.nextDouble(y) - y / 2;
        double z2 = z == 0 ? 0 : U.r.nextDouble(z) - z / 2;
        Vector3d vec = v.clone();
        return vec.add(x2, y2, z2);
    }
}
