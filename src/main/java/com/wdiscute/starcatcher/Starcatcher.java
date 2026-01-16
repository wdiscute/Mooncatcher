package com.wdiscute.starcatcher;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.wdiscute.starcatcher.component.BobberComponent;
import com.wdiscute.starcatcher.config.FishingConfig;
import com.wdiscute.starcatcher.interaction.FishingInteraction;
import com.wdiscute.starcatcher.storage.Fishes;
import com.wdiscute.starcatcher.systems.BobberSystem;
import com.wdiscute.starcatcher.util.FishHelper;

import javax.annotation.Nonnull;

public class Starcatcher extends JavaPlugin
{

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public static ComponentType<EntityStore, BobberComponent> bobberComponent;
    private final Config<FishingConfig> config;

    public Starcatcher(@Nonnull JavaPluginInit init)
    {
        super(init);
        LOGGER.atInfo().log("Initializing Starcatcher Plugin");
        this.config = this.withConfig("StarcatcherConfig", FishingConfig.CODEC);
    }

    @Override
    protected void shutdown()
    {

    }

    @Override
    protected void setup()
    {
        //register fishes
        Fishes.setup();

        //component
        bobberComponent = this.getEntityStoreRegistry().registerComponent(BobberComponent.class, BobberComponent::new);

        //interaction/codec?
        this.getCodecRegistry(Interaction.CODEC).register("StarcatcherCast", FishingInteraction.class, FishingInteraction.CODEC);

        //system
        this.getEntityStoreRegistry().registerSystem(new BobberSystem());

    }

    @Override
    protected void start()
    {
        super.start();
        this.config.save();
        FishingConfig config = this.config.get();
        FishHelper.setupFishes(config);
    }
}