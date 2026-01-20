package com.wdiscute.mooncatcher.storage;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.World;
import com.wdiscute.mooncatcher.U;
import com.wdiscute.mooncatcher.datapack.DatapackEntry;
import com.wdiscute.mooncatcher.datapack.DatapackReader;

import java.nio.file.Path;
import java.util.*;

//      <><|    <- fish
public class FishProperties implements DatapackEntry
{
    CatchInfo catchInfo;
    int baseChance;
    SizeAndWeight sizeWeight;
    Rarity rarity;
    WorldRestrictions wr;
    BaitRestrictions br;
    Difficulty dif;
    Daytime daytime;
    List<String> weather;
    boolean skipMinigame;
    boolean hasGuideEntry;

    public FishProperties()
    {
    }

    public FishProperties(CatchInfo catchInfo,
                          int baseChance,
                          SizeAndWeight sizeWeight,
                          Rarity rarity,
                          WorldRestrictions wr,
                          BaitRestrictions br,
                          Difficulty dif,
                          Daytime daytime,
                          List<String> weather,
                          boolean skipMinigame,
                          boolean hasGuideEntry
    )
    {
        this.catchInfo = catchInfo;
        this.baseChance = baseChance;
        this.sizeWeight = sizeWeight;
        this.rarity = rarity;
        this.wr = wr;
        this.br = br;
        this.dif = dif;
        this.daytime = daytime;
        this.weather = weather;
        this.skipMinigame = skipMinigame;
        this.hasGuideEntry = hasGuideEntry;
    }

    public static final BuilderCodec<FishProperties> CODEC = BuilderCodec.builder(FishProperties.class, FishProperties::new)
            .append(new KeyedCodec<>("Catch_Info", CatchInfo.CODEC), (fp, o) -> fp.catchInfo = o, fp -> fp.catchInfo)
            .add()
            .append(new KeyedCodec<>("Base_Chance", Codec.INTEGER), (fp, i) -> fp.baseChance = i, fp -> fp.baseChance)
            .add()
//          .append(new KeyedCodec<>("catch_info", SizeAndWeight.CODEC), (fp, o) -> fp.catchInfo = o, fp -> fp.catchInfo)
//          .add()
            .append(new KeyedCodec<>("Rarity", Rarity.CODEC), (fp, o) -> fp.rarity = o, fp -> fp.rarity)
            .add()
            .append(new KeyedCodec<>("World_Restrictions", WorldRestrictions.CODEC), (fp, o) -> fp.wr = o, fp -> fp.wr)
            .add()
            .append(new KeyedCodec<>("Bait_Restrictions", BaitRestrictions.CODEC), (fp, o) -> fp.br = o, fp -> fp.br)
            .add()
            .append(new KeyedCodec<>("Difficulty", Difficulty.CODEC), (fp, o) -> fp.dif = o, fp -> fp.dif)
            .add()
            .append(new KeyedCodec<>("Daytime", Daytime.CODEC), (fp, o) -> fp.daytime = o, fp -> fp.daytime)
            .add()
            .<String[]>append(new KeyedCodec<>("Weather", Codec.STRING_ARRAY), (br, s) -> br.weather = Arrays.stream(s).toList(), wr -> wr.weather.toArray(new String[wr.weather.toArray().length]))
            .add()
            .append(new KeyedCodec<>("Skips_Minigame", Codec.BOOLEAN), (fp, o) -> fp.skipMinigame = o, fp -> fp.skipMinigame)
            .add()
            .append(new KeyedCodec<>("Has_Guide_entry", Codec.BOOLEAN), (fp, o) -> fp.hasGuideEntry = o, fp -> fp.hasGuideEntry)
            .add()
            .build();


    @Override
    public BuilderCodec<?> codec()
    {
        return null;
    }

    public static final FishProperties DEFAULT = new FishProperties(
            CatchInfo.DEFAULT,
            5,
            SizeAndWeight.DEFAULT,
            Rarity.COMMON,
            WorldRestrictions.DEFAULT,
            BaitRestrictions.DEFAULT,
            Difficulty.DEFAULT,
            Daytime.ALL,
            List.of(),
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

    //region CatchInfo
    public static class CatchInfo
    {
        String fish;
        String entityToSpawn;
        boolean alwaysSpawnEntity;
        String overrideMinigameWith;
        String treasure;

        public CatchInfo()
        {
        }

        public CatchInfo(String fish, String entityToSpawn, boolean alwaysSpawnEntity, String overrideMinigameWith, String treasure)
        {
            this.fish = fish;
            this.entityToSpawn = entityToSpawn;
            this.alwaysSpawnEntity = alwaysSpawnEntity;
            this.overrideMinigameWith = overrideMinigameWith;
            this.treasure = treasure;
        }

        public static final BuilderCodec<CatchInfo> CODEC = BuilderCodec.builder(CatchInfo.class, CatchInfo::new)
                .append(new KeyedCodec<>("Fish", Codec.STRING), (ci, s) -> ci.fish = s, ci -> ci.fish)
                .add()
                .append(new KeyedCodec<>("Entity_To_Spawn", Codec.STRING), (ci, s) -> ci.entityToSpawn = s, ci -> ci.entityToSpawn)
                .add()
                .append(new KeyedCodec<>("Always_Spawn_Entity", Codec.BOOLEAN), (ci, s) -> ci.alwaysSpawnEntity = s, ci -> ci.alwaysSpawnEntity)
                .add()
                .append(new KeyedCodec<>("Override_Minigame_With", Codec.STRING), (ci, s) -> ci.overrideMinigameWith = s, ci -> ci.overrideMinigameWith)
                .add()
                .append(new KeyedCodec<>("Treasure", Codec.STRING), (ci, s) -> ci.treasure = s, ci -> ci.treasure)
                .add()
                .build();


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

    public static class BaitRestrictions
    {
        List<String> correctBait;
        boolean consumesBait;
        int correctBaitChanceAdded;

        public BaitRestrictions()
        {

        }

        public BaitRestrictions(List<String> correctBait, boolean consumesBait, int correctBaitChanceAdded)
        {
            this.correctBait = correctBait;
            this.consumesBait = consumesBait;
            this.correctBaitChanceAdded = correctBaitChanceAdded;
        }


        public static final BuilderCodec<BaitRestrictions> CODEC = BuilderCodec.builder(BaitRestrictions.class, BaitRestrictions::new)
                .<String[]>append(new KeyedCodec<>("Correct_Baits", Codec.STRING_ARRAY), (br, s) -> br.correctBait = Arrays.stream(s).toList(), br -> br.correctBait.toArray(new String[br.correctBait.toArray().length]))
                .add()
                .append(new KeyedCodec<>("Consumes_Bait", Codec.BOOLEAN), (br, s) -> br.consumesBait = s, br -> br.consumesBait)
                .add()
                .append(new KeyedCodec<>("Correct_Bait_Chance_Added", Codec.INTEGER), (br, s) -> br.correctBaitChanceAdded = s, br -> br.correctBaitChanceAdded)
                .add()
                .build();

        public static final BaitRestrictions DEFAULT = new BaitRestrictions(
                List.of(),
                true,
                0);

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
    public static class WorldRestrictions
    {
        List<String> environments;
        List<String> environmentsBlacklist;
        List<String> fluids;
        int mustBeCaughtBelowY;
        int mustBeCaughtAboveY;

        public WorldRestrictions()
        {
        }

        public WorldRestrictions(List<String> environments, List<String> environmentsBlacklist, List<String> fluids, int mustBeCaughtBelowY, int mustBeCaughtAboveY)
        {
            this.environments = environments;
            this.environmentsBlacklist = environmentsBlacklist;
            this.fluids = fluids;
            this.mustBeCaughtBelowY = mustBeCaughtBelowY;
            this.mustBeCaughtAboveY = mustBeCaughtAboveY;
        }

        public static final BuilderCodec<WorldRestrictions> CODEC = BuilderCodec.builder(WorldRestrictions.class, WorldRestrictions::new)
                .<String[]>append(new KeyedCodec<>("Environments", Codec.STRING_ARRAY), (br, s) -> br.environments = Arrays.stream(s).toList(), wr -> wr.environments.toArray(new String[wr.environments.toArray().length]))
                .add()
                .<String[]>append(new KeyedCodec<>("Environments_Blacklist", Codec.STRING_ARRAY), (br, s) -> br.environmentsBlacklist = Arrays.stream(s).toList(), wr -> wr.environmentsBlacklist.toArray(new String[wr.environmentsBlacklist.toArray().length]))
                .add()
                .<String[]>append(new KeyedCodec<>("Fluids", Codec.STRING_ARRAY), (br, s) -> br.fluids = Arrays.stream(s).toList(), wr -> wr.fluids.toArray(new String[wr.fluids.toArray().length]))
                .add()
                .append(new KeyedCodec<>("Bellow_Y", Codec.INTEGER), (wr, s) -> wr.mustBeCaughtBelowY = s, wr -> wr.mustBeCaughtBelowY)
                .add()
                .append(new KeyedCodec<>("Above_Y", Codec.INTEGER), (wr, s) -> wr.mustBeCaughtAboveY = s, wr -> wr.mustBeCaughtAboveY)
                .add()
                .build();

        public static final WorldRestrictions DEFAULT = new WorldRestrictions(
                List.of(),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_1_RIVERS = new WorldRestrictions(
                List.of("#Env_Zone1"),
                List.of("#Env_Zone1_Caves", "Env_Zone1_Shores"),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_1_AUTUMN = new WorldRestrictions(
                List.of("Env_Zone1_Autumn"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions OCEANS = new WorldRestrictions(
                List.of("#Env_Zone0", "#Shores"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_0 = new WorldRestrictions(
                List.of("#Env_Zone0"),
                List.of(),
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

        public static final WorldRestrictions ZONE_2 = new WorldRestrictions(
                List.of("#Env_Zone2"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);


        public static final WorldRestrictions ZONE_2_DESERTS = new WorldRestrictions(
                List.of("Env_Zone2_Deserts"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_2_SAVANNA = new WorldRestrictions(
                List.of("Env_Zone2_Savanna"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_3 = new WorldRestrictions(
                List.of("#Env_Zone3"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_3_GLACIAL = new WorldRestrictions(
                List.of("Env_Zone3_Glacial"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_3_SHORE = new WorldRestrictions(
                List.of("Env_Zone3_Shore"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final WorldRestrictions ZONE_3_FORESTS = new WorldRestrictions(
                List.of("Env_Zone3_Forests"),
                List.of(),
                List.of("Water"),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);
    }

    //region dif
    public static class Difficulty
    {
        int speed;
        int penalty;
        float decay;

        public Difficulty()
        {
        }

        public Difficulty(int speed, int penalty, float decay)
        {
            this.speed = speed;
            this.penalty = penalty;
            this.decay = decay;
        }

        public static final BuilderCodec<Difficulty> CODEC = BuilderCodec.builder(Difficulty.class, Difficulty::new)
                .append(new KeyedCodec<>("Speed", Codec.INTEGER), (dif, s) -> dif.speed = s, dif -> dif.speed)
                .add()
                .append(new KeyedCodec<>("Penalty", Codec.INTEGER), (dif, s) -> dif.penalty = s, dif -> dif.penalty)
                .add()
                .append(new KeyedCodec<>("Decay", Codec.FLOAT), (dif, s) -> dif.decay = s, dif -> dif.decay)
                .add()
                .build();

        public static final Difficulty DEFAULT = new Difficulty(10, 10, 1);
    }

    //endregion dif


    public record SizeAndWeight(float sizeAverage, float sizeDeviation, float weightAverage, float weightDeviation)
    {
        public static final SizeAndWeight DEFAULT = new SizeAndWeight(41f, 21f, 2001f, 701f);
    }

    public enum Rarity
    {
        COMMON(40),
        UNCOMMON(40),
        RARE(30),
        EPIC(10),
        LEGENDARY(10);

        private final int stoneHookGraceTicks;

        Rarity(int stoneHookGraceTicks)
        {
            this.stoneHookGraceTicks = stoneHookGraceTicks;
        }

        public static final EnumCodec<Rarity> CODEC = new EnumCodec<>(Rarity.class);
    }

    public enum Daytime
    {
        ALL,
        DAY,
        NOON,
        NIGHT,
        MIDNIGHT;

        public static final EnumCodec<Daytime> CODEC = new EnumCodec<>(Daytime.class);
    }

    public static SizeAndWeight sizeWeight(float sizeAvg, float sizeDev, float weightAvg, float weightDev)
    {
        return new SizeAndWeight(sizeAvg, sizeDev, weightAvg, weightDev);
    }

    public static List<FishProperties> getFishesForRestrictions(World world, Vector3d blockPos)
    {
        List<FishProperties> list = new ArrayList<>();
        for (FishProperties fp : FishProperties.getFishes())
        {
            int chance = getChance(fp, world, blockPos);
            for (int i = 0; i < chance; i++)
            {
                list.add(fp);
            }
        }
        return list;
    }

    private static List<FishProperties> getFishes()
    {
        DatapackReader<FishProperties> reader = new DatapackReader<>(Path.of("mooncatcher"), "fishes", FishProperties.CODEC, new Fishes());
        return reader.getDataValues().stream().toList();
    }


    public static int getChance(FishProperties fp, World world, Vector3d blockPos)
    {
        if (!isEnvironmentCorrect(U.getEnvName(world, blockPos), fp)) return 0;
        if (!isWeatherCorrect(U.getWeatherName(world, blockPos), fp)) return 0;
        if (!isDaytimeCorrect(U.getDaytimePercentage(world, blockPos), fp)) return 0;

        //add fluid
        return fp.baseChance;
    }

    public static boolean isDaytimeCorrect(float dayProgress, FishProperties fp)
    {
        switch (fp.daytime)
        {
            case Daytime.DAY:
                if (dayProgress >= 5 && dayProgress <= 19.5f) return false;
                break;

            case Daytime.NOON:
                if (dayProgress <= 10 || dayProgress >= 14) return false;
                break;

            case Daytime.NIGHT:
                if (dayProgress >= 19.5f || dayProgress <= 5) return false;
                break;

            case Daytime.MIDNIGHT:
                if (dayProgress <= 2 || dayProgress >= 22) return false;
                break;
        }

        return true;
    }


    public static boolean isWeatherCorrect(String weather, FishProperties fp)
    {
        if (fp.weather.isEmpty()) return true;
        for (String s : fp.weather)
        {
            if (s.contains("#"))
            {
                String substring = s.substring(1);
                if (weather.contains(substring)) return true;
            } else
            {
                if (weather.equals(s)) return true;
            }
        }
        return false;
    }

    public static boolean isEnvironmentCorrect(String bobberEnv, FishProperties fp)
    {
        for (String s : fp.wr.environments)
        {
            if (s.contains("#"))
            {
                String substring = s.substring(1);
                if (bobberEnv.contains(substring)) return true;
            } else
            {
                if (bobberEnv.equals(s)) return true;
            }
        }
        return false;
    }


}
