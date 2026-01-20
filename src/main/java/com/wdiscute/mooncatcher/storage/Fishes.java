package com.wdiscute.mooncatcher.storage;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.util.InventoryHelper;
import com.wdiscute.mooncatcher.U;
import com.wdiscute.mooncatcher.components.BobberComponent;
import com.wdiscute.mooncatcher.datapack.DatapackProvider;
import it.unimi.dsi.fastutil.Pair;

import java.util.ArrayList;
import java.util.List;

public class Fishes extends DatapackProvider<FishProperties>
{
    private static final List<FishProperties> fishes = new ArrayList<>();

    private void register(FishProperties fp)
    {
        fishes.add(fp);
    }

    public static ItemStack getFish(World world, Vector3d blockPos, BobberComponent bobberComponent, CommandBuffer<EntityStore> entityStore)
    {
        List<FishProperties> list = FishProperties.getFishesForRestrictions(world, blockPos);

        if(list.isEmpty()) return ItemStack.EMPTY;

        return InventoryHelper.createItem(list.get(U.r.nextInt(list.size() - 1)).catchInfo.fish);
    }

    public void setup()
    {
        //ZONE 1 RIVERS
        register(overworldZone1Fish("Bluegigi")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_RIVERS)
        );

        register(overworldZone1Fish("Carpenjoe")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_RIVERS)
        );

        register(overworldZone1Fish("Joel")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_RIVERS)
        );

        register(overworldZone1Fish("Fish_Catfish_Item")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_RIVERS)
        );

        register(overworldZone1Fish("Fish_Minnow_Item")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_RIVERS)
        );

        //ZONE 1 AUTUMN
        register(overworldZone1Fish("Petaldrift_Carp")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_RIVERS)
        );


        //ZONE 1 AZURE
        register(overworldZone1Fish("Vesani")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_AZURE)
        );

        register(overworldZone1Fish("Ward")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_AZURE)
        );

        //ZONE 1 VOLCANIC
        register(overworldZone1Fish("Pyrotrout")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_CAVES)
                .withDaytime(FishProperties.Daytime.DAY)
                .withRarity(FishProperties.Rarity.RARE)
        );

        register(overworldZone1Fish("Basaltish")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_CAVES)
                .withDaytime(FishProperties.Daytime.DAY)
                .withRarity(FishProperties.Rarity.RARE)
        );

        register(overworldZone1Fish("Scalding_Pike")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_1_CAVES)
                .withDaytime(FishProperties.Daytime.DAY)
                .withRarity(FishProperties.Rarity.RARE)
        );

        //ZONE 2
        register(overworldZone1Fish("Sun_Seeking_Carp")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_2)
                .withDaytime(FishProperties.Daytime.DAY)
                .withRarity(FishProperties.Rarity.RARE)
        );

        //ZONE 2 DESERTS
        register(overworldZone1Fish("Sphynx")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_2_DESERTS)
                .withDaytime(FishProperties.Daytime.DAY)
                .withRarity(FishProperties.Rarity.RARE)
        );

        //ZONE 2 SAVANNA
        register(overworldZone1Fish("Suneater")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_2_DESERTS)
                .withDaytime(FishProperties.Daytime.DAY)
                .withRarity(FishProperties.Rarity.RARE)
        );


        //ZONE 3 GLACIAL
        register(overworldZone1Fish("Aurora")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_3_GLACIAL)
        );


        //ZONE 3 SHORE
        register(overworldZone1Fish("Azure_Crystalback_Minnow")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_3_SHORE)
        );


        //ZONE 3 EVERYWHERE
        register(overworldZone1Fish("Crystalback_Minnow")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_3)
        );

        register(overworldZone1Fish("Blue_Crystal_Fin")
                .withWorldRestrictions(FishProperties.WorldRestrictions.ZONE_3)
        );



        //OCEANS
        register(overworldZone1Fish("Ironjaw_Herring")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OCEANS)
                .withDaytime(FishProperties.Daytime.DAY)
                .withRarity(FishProperties.Rarity.RARE)
        );

        register(overworldZone1Fish("Redscaled_Tuna")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OCEANS)
                .withDaytime(FishProperties.Daytime.DAY)
                .withRarity(FishProperties.Rarity.RARE)
        );

    }

    private static FishProperties overworldZone1Fish(String fish)
    {
        return FishProperties.DEFAULT.withFish(fish);
    }

    @Override
    public List<Pair<String, FishProperties>> generatePack(List<Pair<String, FishProperties>> packContents)
    {
        setup();

        for (FishProperties fp : fishes)
            add(fp, fp.catchInfo.fish, packContents);

        return packContents;
    }
}
