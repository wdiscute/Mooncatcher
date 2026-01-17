package com.wdiscute.mooncatcher.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.*;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.wdiscute.mooncatcher.components.BobberComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.UUID;

public class FishingInteraction extends SimpleInstantInteraction
{
    public static final BuilderCodec<FishingInteraction> CODEC =
            BuilderCodec.builder(FishingInteraction.class, FishingInteraction::new, SimpleInstantInteraction.CODEC)
                    .documentation("Throws a bobber out into the wild! Who knows what it might catch, perhaps a star...")
                    .build();

    @Override
    protected void firstRun(@NonNullDecl InteractionType type, @NonNullDecl InteractionContext context, @NonNullDecl CooldownHandler handler)
    {
        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        World world = commandBuffer.getExternalData().getWorld();
        Ref<EntityStore> playerRef = context.getEntity();
        Player player = commandBuffer.getComponent(playerRef, Player.getComponentType());
        BobberComponent bobberComp = commandBuffer.getComponent(playerRef, BobberComponent.getComponentType());

        //cast
        if (bobberComp == null)
        {
            int soundEventIndex = SoundEvent.getAssetMap().getIndex("SFX_Starcatcher_Cast");
            SoundUtil.playSoundEvent2dToPlayer(player.getPlayerRef(), soundEventIndex, SoundCategory.SFX);
            Vector3d pos = player.getTransformComponent().getPosition();

            Vector3d direction = TargetUtil.getLook(playerRef, commandBuffer).getDirection();
            BobberComponent bobberComponent = new BobberComponent(world, playerRef, player);
            spawnBobber(commandBuffer, context, pos, direction, bobberComponent);

            //add bobber component to player
            commandBuffer.addComponent(playerRef, BobberComponent.getComponentType(), bobberComponent);
        }
        //retrieve
        else
        {
            bobberComp.reel(commandBuffer);
            int soundEventIndex = SoundEvent.getAssetMap().getIndex("SFX_Starcatcher_Reel");

            //noinspection removal
            SoundUtil.playSoundEvent2dToPlayer(player.getPlayerRef(), soundEventIndex, SoundCategory.SFX);
        }
    }

    private void spawnBobber(CommandBuffer<EntityStore> commandBuffer, InteractionContext context,
                             Vector3d pos, Vector3d direction, BobberComponent bobberComponent)
    {
        //make new entity holder
        Ref<EntityStore> ref = context.getEntity();
        Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();

        //transform comp
        holder.addComponent(TransformComponent.getComponentType(), new TransformComponent(pos.clone().add(0, 1.4f, 0), new Vector3f()));

        //Velocity comp
        direction.normalize();
        direction.scale(1);
        holder.addComponent(Velocity.getComponentType(), new Velocity(new Vector3d(direction.x, direction.y, direction.z)));

        //uuid comp
        UUID uuid = UUID.randomUUID();
        holder.addComponent(UUIDComponent.getComponentType(), new UUIDComponent(uuid));

        //network id comp
        holder.putComponent(NetworkId.getComponentType(), new NetworkId(ref.getStore().getExternalData().takeNextNetworkId()));

        //model asset stuff??????? why is it needed
        ModelAsset modelasset = ModelAsset.getAssetMap().getAsset("StarcatcherBobber");
        if (modelasset == null) modelasset = ModelAsset.DEBUG;

        //create model i guess????????????????????????
        Model model = Model.createRandomScaleModel(modelasset);

        //PersistentModel comp???
        holder.addComponent(PersistentModel.getComponentType(), new PersistentModel(model.toReference()));

        //Model comp??
        holder.addComponent(ModelComponent.getComponentType(), new ModelComponent(model));

        if (model.getBoundingBox() == null) throw new IllegalArgumentException("bounding box of model is null");

        //bounding box comp??
        holder.addComponent(BoundingBox.getComponentType(), new BoundingBox(model.getBoundingBox()));

        //add bobber component
        holder.addComponent(BobberComponent.getComponentType(), bobberComponent);

        //add head component
        holder.addComponent(HeadRotation.getComponentType(), new HeadRotation());

        //adds entity
        Ref<EntityStore> entityStoreRef = commandBuffer.addEntity(holder, AddReason.SPAWN);

        //store bobberRef
        bobberComponent.setRef(entityStoreRef);
    }
}
