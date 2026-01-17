package com.wdiscute.mooncatcher.storage;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.util.InventoryHelper;
import com.wdiscute.mooncatcher.U;
import com.wdiscute.mooncatcher.components.BobberComponent;

import java.util.ArrayList;
import java.util.List;

public class Fishes
{
    static List<FishProperties> fishes = new ArrayList<>();

    public static void register(FishProperties fp)
    {
        fishes.add(fp);
    }

    public static ItemStack getFish(World world, Vector3d blockPos, BobberComponent bobberComponent, CommandBuffer<EntityStore> entityStore)
    {
        List<FishProperties> list = FishProperties.getFishesForRestrictions(world, blockPos);

        if(list.isEmpty()) return ItemStack.EMPTY;

        return InventoryHelper.createItem(list.get(U.r.nextInt(list.size() - 1)).catchInfo().fish());
    }

    public static void setup()
    {
        register(overworldZone1Fish("Starcatcher_Aurora")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_AZURE)
        );

        register(overworldZone1Fish("Rock_Gem_Diamond")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_SURFACE)
                .withWeather(FishProperties.Weather.RAIN)
                .withRarity(FishProperties.Rarity.LEGENDARY)
        );

        register(overworldZone1Fish("Rock_Gem_Emerald")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_SURFACE)
                .withDaytime(FishProperties.Daytime.DAY)
                .withWeather(FishProperties.Weather.CLEAR)
                .withRarity(FishProperties.Rarity.RARE)
        );

        register(overworldZone1Fish("Rock_Gem_Ruby")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_CAVES)
                .withWeather(FishProperties.Weather.THUNDER)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(overworldZone1Fish("Rock_Gem_Sapphire")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_CAVES)
                .withRarity(FishProperties.Rarity.COMMON)
        );

    }

    private static FishProperties overworldZone1Fish(String fish)
    {
        return FishProperties.DEFAULT.withFish(fish);
    }
}
