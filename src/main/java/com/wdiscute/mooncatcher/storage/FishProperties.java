package com.wdiscute.mooncatcher.storage;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.World;
import com.wdiscute.mooncatcher.U;

import java.util.ArrayList;
import java.util.List;

//      <><|    <- fish
public record FishProperties(
        CatchInfo catchInfo,
        int baseChance,
        SizeAndWeight sizeWeight,
        Rarity rarity,
        WorldRestrictions wr,
        BaitRestrictions br,
        Difficulty dif,
        Daytime daytime,
        Weather weather,
        boolean skipMinigame,
        boolean hasGuideEntry
)
{
    public static final FishProperties DEFAULT = new FishProperties(
            CatchInfo.DEFAULT,
            5,
            SizeAndWeight.DEFAULT,
            Rarity.COMMON,
            WorldRestrictions.DEFAULT,
            BaitRestrictions.DEFAULT,
            Difficulty.DEFAULT,
            Daytime.ALL,
            Weather.ALL,
            false,
            true
    );

    public FishProperties withFish(String fish)
    {
        return withCatchInfo(new CatchInfo(fish, this.catchInfo.entityToSpawn, this.catchInfo.alwaysSpawnEntity, this.catchInfo.overrideMinigameWith, this.catchInfo.treasure));
    }

    public FishProperties withCatchInfo(CatchInfo catchInfo)
    {
        return new FishProperties(catchInfo, this.baseChance, this.sizeWeight, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withSizeAndWeight(SizeAndWeight sizeAndWeight)
    {
        return new FishProperties(this.catchInfo, this.baseChance, sizeAndWeight, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withWorldRestrictions(WorldRestrictions worldRestrictions)
    {
        return new FishProperties(this.catchInfo, this.baseChance, this.sizeWeight, this.rarity, worldRestrictions, this.br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withBaitRestrictions(BaitRestrictions baitRestrictions)
    {
        return new FishProperties(this.catchInfo, this.baseChance, this.sizeWeight, this.rarity, this.wr, baitRestrictions, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withRarity(Rarity rarity)
    {
        return new FishProperties(this.catchInfo, this.baseChance, this.sizeWeight, rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withDaytime(Daytime daytime)
    {
        return new FishProperties(this.catchInfo, this.baseChance, this.sizeWeight, rarity, this.wr, this.br, this.dif, daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withWeather(Weather weather)
    {
        return new FishProperties(this.catchInfo, this.baseChance, this.sizeWeight, rarity, this.wr, this.br, this.dif, this.daytime, weather, this.skipMinigame, this.hasGuideEntry);
    }

    //region CatchInfo
    public record CatchInfo(
            String fish,
            String entityToSpawn,
            boolean alwaysSpawnEntity,
            String overrideMinigameWith,
            String treasure
    )
    {
        public CatchInfo withFish(String fish)
        {
            return new CatchInfo(fish, this.entityToSpawn, this.alwaysSpawnEntity, this.overrideMinigameWith, this.treasure);
        }

        public static final CatchInfo DEFAULT = new CatchInfo(
                "Aurora",
                "Aurora",
                false,
                "Aurora",
                "Aurora"
        );
    }

    //endregion CatchInfo


    //region bait

    public record BaitRestrictions(
            List<String> correctBait,
            boolean consumesBait,
            int correctBaitChanceAdded
    )
    {
        public static final BaitRestrictions DEFAULT = new BaitRestrictions(
                List.of(),
                true,
                0);

        public static final BaitRestrictions CHERRY_BAIT = new BaitRestrictions(
                List.of("Cherry_Bait"),
                true,
                15);

        public static final BaitRestrictions LEGENDARY_BAIT = new BaitRestrictions(
                List.of("Legendary_Bait"),
                true,
                15);

        public BaitRestrictions withCorrectBait(String... correctBait)
        {
            return new BaitRestrictions(List.of(correctBait), this.consumesBait, this.correctBaitChanceAdded);
        }

        public BaitRestrictions withConsumesBait(boolean consumesBait)
        {
            return new BaitRestrictions(this.correctBait, consumesBait, this.correctBaitChanceAdded);
        }

        public BaitRestrictions withCorrectBaitChanceAdded(int correctBaitChanceAdded)
        {
            return new BaitRestrictions(this.correctBait, consumesBait, correctBaitChanceAdded);
        }
    }

    //endregion bait

    //region world
    public record WorldRestrictions(
            List<String> environments,
            List<String> environmentsBlacklist,
            List<String> fluids,
            int mustBeCaughtBelowY,
            int mustBeCaughtAboveY
    )
    {

        public static final WorldRestrictions DEFAULT = new WorldRestrictions(
                List.of(),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_1_SURFACE = new WorldRestrictions(
                List.of("#Env_Zone1"),
                List.of("#Env_Zone1_Caves"),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_1_CAVES = new WorldRestrictions(
                List.of("#Env_Zone1_Caves"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_1_AZURE = new WorldRestrictions(
                List.of("Env_Zone1_Azure"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_2_SURFACE = new WorldRestrictions(
                List.of("Env_Zone1_Azure"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);
    }

    //region dif
    public record Difficulty(
            int speed,
            int penalty,
            float decay
    )
    {
        public static final Difficulty DEFAULT = new Difficulty(10, 10, 1);
    }

    //endregion dif


    public record SizeAndWeight(float sizeAverage, float sizeDeviation, float weightAverage, float weightDeviation)
    {
        public static final SizeAndWeight DEFAULT = new SizeAndWeight(41f, 21f, 2001f, 701f);
        public static final SizeAndWeight NONE = new SizeAndWeight(0, 0, 0, 0);
    }

    public enum Rarity
    {
        COMMON("common",  40),
        UNCOMMON("uncommon", 40),
        RARE("rare", 30),
        EPIC("epic",  10),
        LEGENDARY("legendary", 10);

        private final String key;
        private final int stoneHookGraceTicks;

        Rarity(String key, int stoneHookGraceTicks)
        {
            this.key = key;
            this.stoneHookGraceTicks = stoneHookGraceTicks;
        }

        public String getSerializedName()
        {
            return this.key;
        }

        public int getStoneHookGraceTicks()
        {
            return stoneHookGraceTicks;
        }

        public int getId()
        {
            return this.ordinal();
        }
    }

    public enum Daytime
    {
        ALL("all"),
        DAY("day"),
        NOON("noon"),
        NIGHT("night"),
        MIDNIGHT("midnight");

        private final String key;

        Daytime(String key)
        {
            this.key = key;
        }
    }

    public enum Weather
    {
        ALL("all"),
        CLEAR("clear"),
        RAIN("rain"),
        THUNDER("thunder");

        private final String key;

        Weather(String key)
        {
            this.key = key;
        }

        public String getSerializedName()
        {
            return this.key;
        }
    }

    public static SizeAndWeight sizeWeight(float sizeAvg, float sizeDev, float weightAvg, float weightDev)
    {
        return new SizeAndWeight(sizeAvg, sizeDev, weightAvg, weightDev);
    }

    public static List<FishProperties> getFishesForRestrictions(World world, Vector3d blockPos)
    {
        List<FishProperties> list = new ArrayList<>();
        for (FishProperties fp : Fishes.fishes)
        {
            int chance = getChance(fp, world, blockPos);
            for (int i = 0; i < chance; i++)
            {
                list.add(fp);
            }
        }
        return list;
    }


    public static int getChance(FishProperties fp, World world, Vector3d blockPos)
    {
        if(!isEnvironmentCorrect(U.getEnvName(world, blockPos), fp)) return 0;

        return fp.baseChance;
    }

    public static boolean isEnvironmentCorrect(String bobberEnv, FishProperties fp)
    {
        for (String s : fp.wr.environments)
        {
            if(s.contains("#"))
            {
                String substring = s.substring(1);
                if(bobberEnv.contains(substring)) return true;
            }
            else
            {
                if(bobberEnv.equals(s)) return true;
            }
        }
        return false;
    }





}
