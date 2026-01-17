package com.wdiscute.mooncatcher.components;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.AnimationUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.wdiscute.mooncatcher.Mooncatcher;
import com.wdiscute.mooncatcher.U;
import com.wdiscute.mooncatcher.storage.FishProperties;
import com.wdiscute.mooncatcher.storage.Fishes;

public class BobberComponent implements Component<EntityStore>
{
    private int ticks;
    World world;
    FishProperties fpToCatch;

    Player player;
    Ref<EntityStore> bobberRef;
    Ref<EntityStore> playerRef;
    boolean removed = false;

    public int minTicksToFish = 100;
    public int maxTicksToFish = 300;
    public int chanceToFishEachTick = 100;

    FishingState currentState = FishingState.FLYING;

    public int timeBiting = 0;
    public int timeBobbing = 0;
    boolean oInsideWater = false;

    public BobberComponent()
    {
        this.ticks = 0;
        this.world = Universe.get().getDefaultWorld();
        this.fpToCatch = null;
        this.bobberRef = null;
    }

    public BobberComponent(World world, Ref<EntityStore> playerRef, Player player)
    {
        this.ticks = 0;
        this.world = world;
        this.fpToCatch = null;
        this.playerRef = playerRef;
        this.bobberRef = null;
        this.player = player;
    }

    public static ComponentType<EntityStore, BobberComponent> getComponentType()
    {
        return Mooncatcher.bobberComponent;
    }

    public static boolean isInsideWater(World world, Vector3d origin)
    {
        return TargetUtil.getTargetBlock(
                world,
                (_, fluidId) -> fluidId != 0,
                origin.x, origin.y + 0.2f, origin.z, Vector3d.DOWN.x, Vector3d.DOWN.y, Vector3d.DOWN.z, 0f
        ) != null;
    }

    public void tickBobber(Vector3d pos, CommandBuffer<EntityStore> commandBuffer)
    {
        ticks++;
        boolean insideWater = isInsideWater(world, pos) || isInsideWater(world, pos.clone().add(0, -0.5f, 0));

        System.out.println(insideWater);

        //spawn splash particles on entering water
        if (oInsideWater != insideWater)
                ParticleUtil.spawnParticleEffect("Splash_System", U.offsetVectorByRandom(commandBuffer.getComponent(bobberRef, TransformComponent.getComponentType()).getPosition().clone(), 0f, 0.2f, 0f), commandBuffer);

        oInsideWater = insideWater;

        //spawn particles sometimes when inside water
        if (insideWater && (ticks % 17 == 0 || ticks % 36 == 0))
            ParticleUtil.spawnParticleEffect("Splash_System", U.offsetVectorByRandom(commandBuffer.getComponent(bobberRef, TransformComponent.getComponentType()).getPosition().clone(), 0.4f, 0, 0.4f), commandBuffer);


        //flying
        if (this.currentState == FishingState.FLYING)
        {
            AnimationUtils.playAnimation(bobberRef, AnimationSlot.Status, "Idle", true, commandBuffer);

            if (insideWater)
            {
                this.currentState = FishingState.BOBBING;
                return;
            }
        }

        //biting
        if (this.currentState == FishingState.BITING)
        {
            timeBiting++;
            AnimationUtils.playAnimation(bobberRef, AnimationSlot.Status, "Biting", true, commandBuffer);

            //spawn extra particles when biting
            if (U.r.nextFloat() < 0.4f)
                ParticleUtil.spawnParticleEffect("Splash_System", U.offsetVectorByRandom(commandBuffer.getComponent(bobberRef, TransformComponent.getComponentType()).getPosition().clone(), 0.8f, 0.4f, 0.8f), commandBuffer);

            //todo spawn particles
            if (timeBiting > 150)
            {
                //player.sendMessage(Message.raw("damn, missed it..."));
                commandBuffer.removeEntity(bobberRef, RemoveReason.REMOVE);
                commandBuffer.removeComponent(playerRef, BobberComponent.getComponentType());
                removed = true;
            }
        } else
        {
            timeBiting = 0;
        }

        //if not inside water, changes to FLYING
        if (!insideWater)
        {
            currentState = FishingState.FLYING;
        }

        if (this.currentState == FishingState.BOBBING)
        {
            if (timeBobbing == 100)
            {
                //player.sendMessage(Message.raw("and..."));
            }
            checkForFish();
        } else
        {
            timeBobbing = 0;
        }

    }

    private void checkForFish()
    {
        if (currentState == FishingState.BOBBING)
        {
            timeBobbing++;
            int i = U.r.nextInt(chanceToFishEachTick);
            if ((i == 1 || timeBobbing > maxTicksToFish) && timeBobbing > minTicksToFish)
            {
                TransformComponent transformComponent = bobberRef.getStore().getComponent(bobberRef, TransformComponent.getComponentType());
                transformComponent.setPosition(transformComponent.getPosition().add(0, -0.3, 0));
                currentState = FishingState.BITING;

                //player.sendMessage(Message.raw("now!"));

                //todo play splash sound
                //this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
            }
        }

    }

    public void reel(CommandBuffer<EntityStore> store)
    {
        if (removed) return;
        if (currentState == FishingState.BITING)
        {
            //todo minigame
            currentState = FishingState.FISHING;

            //todo set fp based on environment stuff
            ItemStack is = Fishes.getFish(world, store.getComponent(bobberRef, TransformComponent.getComponentType()).getPosition(), this, store);

            //todo item should be awarded on minigame
            if (!is.isEmpty())
            {
                Vector3d bobberPos = store.getComponent(bobberRef, TransformComponent.getComponentType()).getPosition().clone();
                Vector3d playerPos = store.getComponent(playerRef, TransformComponent.getComponentType()).getPosition().clone();

                Vector3d dif2 = bobberPos.clone().subtract(playerPos);
                Vector3d dif = bobberPos.clone();
                dif.subtract(playerPos);
                dif.y = 0;
                dif.normalize();
                dif.scale(-1);
                dif.scale(dif2.length());
                dif.add(0, 15 + -dif2.y / 2.4f, 0);

                float x = (float) dif.x;
                float y = (float) dif.y;
                float z = (float) dif.z;


                Holder<EntityStore> itemEntityHolder = ItemComponent.generateItemDrop(
                        store, is, bobberPos.add(0, 0.5f, 0),
                        Vector3f.ZERO, x, y, z
                );

                store.addEntity(itemEntityHolder, AddReason.SPAWN);
            }
            //todo here it should return; so it doesnt remove entity and component whilst minigame is happening
        }

        //remove if reeled with no bite
        store.removeEntity(bobberRef, RemoveReason.REMOVE);
        store.removeComponent(playerRef, BobberComponent.getComponentType());
        this.removed = true;
    }

    @Override
    public Component<EntityStore> clone()
    {
        BobberComponent component = new BobberComponent();
        component.ticks = this.ticks;
        component.fpToCatch = this.fpToCatch;
        return component;
    }

    public World world()
    {
        if (world == null) return Universe.get().getDefaultWorld();
        return world;
    }

    public FishingState getState()
    {
        return currentState;
    }

    public void setRef(Ref<EntityStore> bobberRef)
    {
        this.bobberRef = bobberRef;
    }

    public enum FishingState
    {
        FLYING,
        BOBBING,
        BITING,
        FISHING
    }
}