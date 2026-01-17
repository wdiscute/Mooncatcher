package com.wdiscute.mooncatcher.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSkinComponent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.wdiscute.mooncatcher.Mooncatcher;
import com.wdiscute.mooncatcher.components.BobberComponent;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;

public class BobberSystem extends EntityTickingSystem<EntityStore>
{

    public static boolean isBlockOnDirection(World world, Vector3d origin, Vector3d direction)
    {
        //bipredicate already checks if fluid is water (not 0, might also work for lava?)
        return TargetUtil.getTargetBlock(
                world,
                (block, _) -> block != 0,
                origin.x + direction.x / 2, origin.y + direction.y / 2, origin.z + direction.z / 3, direction.x, direction.y, direction.z, 0f
        ) != null;
    }

    public static boolean isInsideWater(World world, Vector3d origin)
    {
        return TargetUtil.getTargetBlock(
                world,
                (_, fluidId) -> fluidId != 0,
                origin.x, origin.y + 0.2f, origin.z, Vector3d.DOWN.x, Vector3d.DOWN.y, Vector3d.DOWN.z, 0f
        ) != null;
    }

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
                     @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer)
    {
        Ref<EntityStore> bobberRef = archetypeChunk.getReferenceTo(index);
        BobberComponent bobberComp = store.getComponent(bobberRef, BobberComponent.getComponentType());
        var player = store.getComponent(bobberRef, PlayerSkinComponent.getComponentType());
        if(player != null) return;

        Velocity velocity = store.getComponent(bobberRef, Velocity.getComponentType());
        TransformComponent transform = store.getComponent(bobberRef, TransformComponent.getComponentType());
        Vector3d bobberPosition = transform.getPosition();
        World world = bobberComp.world();

        //physics logic
        if (bobberComp.getState().equals(BobberComponent.FishingState.BOBBING) || bobberComp.getState().equals(BobberComponent.FishingState.FLYING))
        {
            velocity.getVelocity();

            double x = velocity.getVelocity().getX();
            double y = velocity.getVelocity().getY();
            double z = velocity.getVelocity().getZ();


            if (y > 0) y = y * 0.9f;

            if (y > -0.3f && !isInsideWater(world, bobberPosition))
            {
                y = y - 0.01f;
            }

            x = x * 0.92f;
            z = z * 0.92f;

            if (isBlockOnDirection(world, bobberPosition, Vector3d.EAST))
            {
                if (x > 0) x = 0;
                z = z * 0.9f;
            }
            if (isBlockOnDirection(world, bobberPosition, Vector3d.WEST))
            {
                if (x < 0) x = 0;
                z = z * 0.9f;
            }
            if (isBlockOnDirection(world, bobberPosition, Vector3d.SOUTH))
            {
                if (z > 0) z = 0;
                z = z * 0.9f;
            }
            if (isBlockOnDirection(world, bobberPosition, Vector3d.NORTH))
            {
                if (z < 0) z = 0;
                z = z * 0.9f;
            }
            if (isBlockOnDirection(world, bobberPosition, Vector3d.DOWN))
            {
                x = x * 0.9f;
                z = z * 0.9f;
                if(y < 0) y = 0;
            }
            if (isBlockOnDirection(world, bobberPosition, Vector3d.UP))
            {
                x = x * 0.9f;
                z = z * 0.9f;
                if(y > 0) y = 0;
            }

            if (isInsideWater(world, bobberPosition))
            {
                y += 0.02f;
            }

            Vector3d newVelocity = new Vector3d(x, y, z);
            velocity.set(newVelocity);
            transform.setPosition(transform.getPosition().add(newVelocity));
        }

        bobberComp.tickBobber(transform.getPosition(), commandBuffer);
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery()
    {
        return Mooncatcher.bobberComponent;
    }
}
