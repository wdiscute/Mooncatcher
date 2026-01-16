package com.wdiscute.starcatcher.storage;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.npc.util.InventoryHelper;

import java.util.ArrayList;
import java.util.List;

public class Fishes
{
    static List<FishProperties> fishes = new ArrayList<>();

    public static void register(FishProperties fp)
    {
        fishes.add(fp);
    }

    public static ItemStack getFish(World world, Vector3d pos)
    {
        return InventoryHelper.createItem(fishes.getFirst().catchInfo().fish());
    }

    public static void setup()
    {
        register(overworldZone1Fish("Aurora")
                .withSizeAndWeight(FishProperties.sizeWeight(17.7f, 5, 1200, 200))
        );

        register(overworldZone1Fish("Rock_Gem_Diamond")
                .withSizeAndWeight(FishProperties.sizeWeight(120, 80, 7000, 1000))
                .withWeather(FishProperties.Weather.RAIN)
                .withRarity(FishProperties.Rarity.LEGENDARY)
        );

        register(overworldZone1Fish("Rock_Gem_Emerald")
                .withSizeAndWeight(FishProperties.sizeWeight(27.0f, 11, 500, 352))
                .withDaytime(FishProperties.Daytime.DAY)
                .withWeather(FishProperties.Weather.CLEAR)
                .withRarity(FishProperties.Rarity.RARE)
        );

        register(overworldZone1Fish("Rock_Gem_Ruby")
                .withSizeAndWeight(FishProperties.sizeWeight(160.0f, 85, 2300, 652))
                .withWeather(FishProperties.Weather.THUNDER)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(overworldZone1Fish("Rock_Gem_Sapphire")
                .withSizeAndWeight(FishProperties.sizeWeight(16.0f, 3, 167, 70))
                .withRarity(FishProperties.Rarity.COMMON)
        );

    }

    private static FishProperties overworldZone1Fish(String fish)
    {
        return FishProperties.DEFAULT.withFish(fish);
    }
}
