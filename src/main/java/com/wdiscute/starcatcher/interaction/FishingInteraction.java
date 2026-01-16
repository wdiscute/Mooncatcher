package com.wdiscute.starcatcher.interaction;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.ItemUtils;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.*;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.wdiscute.starcatcher.component.BobberComponent;
import com.wdiscute.starcatcher.storage.Fishes;
import com.wdiscute.starcatcher.util.FishHelper;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.UUID;

public class FishingInteraction extends SimpleInstantInteraction
{
    public static final BuilderCodec<FishingInteraction> CODEC = BuilderCodec.builder(
                    FishingInteraction.class, FishingInteraction::new, SimpleInstantInteraction.CODEC
            )
            .documentation("Spawns or reels in a bobber when right-clicked on a block with a fishing rod")
            .build();

    @Override
    protected void firstRun(@NonNullDecl InteractionType type, @NonNullDecl InteractionContext context, @NonNullDecl CooldownHandler handler)
    {
        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        World world = commandBuffer.getExternalData().getWorld();
        ItemStack itemstack = context.getHeldItem();
        if (itemstack == null)
        {
            context.getState().state = InteractionState.Failed;
        } else
        {
            Ref<EntityStore> ref = context.getEntity();
            Player player = commandBuffer.getComponent(ref, Player.getComponentType());

            if (player == null)
            {
                context.getState().state = InteractionState.Failed;
                return;
            }

            Inventory inventory = player.getInventory();
            byte activeSlot = inventory.getActiveHotbarSlot();
            ItemStack hotbarItem = inventory.getActiveHotbarItem();

            if (hotbarItem == null)
            {
                context.getState().state = InteractionState.Failed;
                return;
            }

            FishingMetaData fishingMetaData = itemstack.getFromMetadataOrNull(FishingMetaData.KEY, FishingMetaData.CODEC);
            if (fishingMetaData != null)
            {
                // Handle the fishing rod reeling logic
                reelBobber(world, commandBuffer, hotbarItem, inventory, activeSlot, fishingMetaData, player);
            } else
            {
                int soundEventIndex = SoundEvent.getAssetMap().getIndex("SFX_GoneFishing_Cast");
                //noinspection removal
               SoundUtil.playSoundEvent2dToPlayer(player.getPlayerRef(), soundEventIndex, SoundCategory.SFX);

                // Handle the fishing rod casting logic
                //noinspection removal
                Vector3d pos = player.getTransformComponent().getPosition();


                Vector3d direction = TargetUtil.getLook(ref, commandBuffer).getDirection();

                spawnBobber(world, commandBuffer, context, hotbarItem, pos, direction, inventory, activeSlot);
            }
        }
    }

    private void reelBobber(World world, CommandBuffer<EntityStore> commandBuffer, ItemStack hotbarItem, Inventory inventory, byte hotbarSlot, FishingMetaData fishingMetaData, Player player)
    {
        // Remove old bobber and adjust metadata to unbind
        adjustMetadata(inventory, hotbarSlot, hotbarItem, null);

        int soundEventIndex = SoundEvent.getAssetMap().getIndex("SFX_GoneFishing_Reel");

        //noinspection removal
        SoundUtil.playSoundEvent2dToPlayer(player.getPlayerRef(), soundEventIndex, SoundCategory.SFX);

        Ref<EntityStore> bobberRef = world.getEntityStore().getRefFromUUID(fishingMetaData.getFishingUUID());
        if (bobberRef == null) return;

        BobberComponent bobberComp = commandBuffer.getComponent(bobberRef, BobberComponent.getComponentType());
        TransformComponent transformComp = commandBuffer.getComponent(bobberRef, TransformComponent.getComponentType());
        if (bobberComp != null)
        {
            bobberComp.reel(player, transformComp.getPosition(), bobberRef, commandBuffer);
            commandBuffer.removeEntity(bobberRef, RemoveReason.REMOVE);
            return;
        }

        commandBuffer.removeEntity(bobberRef, RemoveReason.REMOVE);
    }

    private void spawnBobber(World world, CommandBuffer<EntityStore> commandBuffer, InteractionContext context, ItemStack fishingStack,
                             Vector3d pos, Vector3d direction,
                             Inventory inventory, byte hotbarSlot)
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

        if (model.getBoundingBox() == null) return;

        //bounding box comp??
        holder.addComponent(BoundingBox.getComponentType(), new BoundingBox(model.getBoundingBox()));

        //add bobber component
        holder.addComponent(BobberComponent.getComponentType(), new BobberComponent(world));

        //add head component
        holder.addComponent(HeadRotation.getComponentType(), new HeadRotation());

        //adds entity
        commandBuffer.addEntity(holder, AddReason.SPAWN);

        // Update the fishing rod's metadata to bind it to the spawned bobber
        adjustMetadata(inventory, hotbarSlot, fishingStack, uuid);
    }

    private void adjustMetadata(Inventory inventory, byte hotbarSlot, @Nonnull ItemStack fishingRod, @Nullable UUID bobberUUID)
    {
        ItemStack newRod;
        if (bobberUUID == null)
        {
            newRod = fishingRod.withMetadata(FishingMetaData.KEY, null);
        } else
        {
            FishingMetaData fishingMetaData = fishingRod.getFromMetadataOrNull(FishingMetaData.KEY, FishingMetaData.CODEC);
            if (fishingMetaData == null)
            {
                fishingMetaData = new FishingMetaData();
            }
            fishingMetaData.setFishingUUID(bobberUUID);
            newRod = fishingRod.withMetadata(FishingMetaData.KEYED_CODEC, fishingMetaData);
        }
        inventory.getHotbar().replaceItemStackInSlot(hotbarSlot, fishingRod, newRod);
    }
}
