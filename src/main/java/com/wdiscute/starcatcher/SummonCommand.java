package com.wdiscute.starcatcher;

import com.hypixel.hytale.assetstore.map.IndexedLookupTableAssetMap;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.environment.config.Environment;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.environment.EnvironmentChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class SummonCommand extends CommandBase
{
    public SummonCommand()
    {
        super("starcatcher", "summons an aurora :)");
        this.setPermissionGroup(GameMode.Adventure); // Allows the command to be used by anyone, not just OP
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx)
    {
        EntityModule.get();

        World world = Universe.get().getDefaultWorld();
        String var = ctx.sender().getDisplayName();

        Store<EntityStore> store = ctx.senderAsPlayerRef().getStore();

        EnvironmentChunk environment = world.getChunk(0).getBlockChunk().getEnvironmentChunk();

        IndexedLookupTableAssetMap<String, Environment> assetMap = Environment.getAssetMap();


        Environment env = assetMap.getAsset("Env_Zone3_Caves_Volcanic");

        System.out.println(store);


        ctx.sendMessage(Message.raw("tried to summon an aurora"));
    }



















}