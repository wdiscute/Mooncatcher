package com.wdiscute.starcatcher.component;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.AnimationUtils;
import com.hypixel.hytale.server.core.entity.ItemUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.interaction.FishingMetaData;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.Fishes;

import java.awt.*;

public class BobberComponent implements Component<EntityStore>
{
    // Max time to wait for a catch
    private static final int MAX_CATCH_TIME = 100;

    private int ticks;
    World world;
    FishProperties fpToCatch;

    public int minTicksToFish = 100;
    public int maxTicksToFish = 300;
    public int chanceToFishEachTick = 100;

    FishingState currentState = FishingState.FLYING;

    public int timeBiting = 0;
    public int ticksInFluid = 0;

    public BobberComponent()
    {
        this.ticks = 0;
        world = Universe.get().getDefaultWorld();
        fpToCatch = null;
    }

    public BobberComponent(World world)
    {
        this.ticks = 0;
        this.world = world;
        fpToCatch = null;
    }

    public static ComponentType<EntityStore, BobberComponent> getComponentType()
    {
        return Starcatcher.bobberComponent;
    }

    public static boolean isInsideWater(World world, Vector3d origin)
    {
        return TargetUtil.getTargetBlock(
                world,
                (_, fluidId) -> fluidId != 0,
                origin.x, origin.y + 0.2f, origin.z, Vector3d.DOWN.x, Vector3d.DOWN.y, Vector3d.DOWN.z, 0f
        ) != null;
    }

    public void tick(Vector3d pos, CommandBuffer<EntityStore> commandBuffer, Ref<EntityStore> bobberRef, Store<EntityStore> store)
    {
        ticks++;
        boolean insideWater = isInsideWater(world, pos);
        System.out.println(timeBiting + " - " + currentState);

        //flying
        if (this.currentState == FishingState.FLYING)
        {
            AnimationUtils.playAnimation(bobberRef, AnimationSlot.Status, "Idle", true, store);

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
            AnimationUtils.playAnimation(bobberRef, AnimationSlot.Status, "Biting", true, store);
            //todo spawn particles
            if (timeBiting > 150)
            {
                //todo reset fishing rod data
                commandBuffer.removeEntity(bobberRef, RemoveReason.REMOVE);
            }
        }
        else
        {
            timeBiting = 0;
        }

        //if not inside water, changes to FLYING
        if (!insideWater && !isInsideWater(world, pos.clone().add(Vector3d.DOWN)))
        {
            currentState = FishingState.FLYING;
        }

        if (this.currentState == FishingState.BOBBING || this.currentState == FishingState.FISHING)
        {
            checkForFish();
        }
    }

    private void checkForFish()
    {
        if (currentState == FishingState.BOBBING)
        {
            ticksInFluid++;
            int i = U.r.nextInt(chanceToFishEachTick);
            if ((i == 1 || ticksInFluid > maxTicksToFish) && ticksInFluid > minTicksToFish)
            {
                //todo lower bobber when biting
                //this.setPos(position().x, position().y - 0.5f, position().z);
                currentState = FishingState.BITING;

                //todo play splash sound
                //this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
            }
        }

    }

    public void reel(Player player, Vector3d pos, Ref<EntityStore> bobberRef, CommandBuffer<EntityStore> commandBuffer)
    {
        if (currentState == FishingState.BITING)
        {
            //todo minigame
            currentState = FishingState.FISHING;

            //todo item should be awarded on minigame
            ItemStack is = Fishes.getFish(world, pos);
            if (!is.isEmpty())
            {
                ItemUtils.throwItem(bobberRef, is, 5.0F, commandBuffer);
                player.sendMessage(Message.translation("gonefishing.caughtFish").color(Color.GREEN).param("fish", Message.translation(is.getItem().getTranslationKey())));
            }
        }
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

    public enum FishingState
    {
        FLYING,
        BOBBING,
        BITING,
        FISHING
    }
}