package com.wdiscute.starcatcher;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.wdiscute.starcatcher.components.BobberComponent;
import com.wdiscute.starcatcher.interactions.FishingInteraction;
import com.wdiscute.starcatcher.storage.Fishes;
import com.wdiscute.starcatcher.systems.BobberSystem;

import javax.annotation.Nonnull;

public class Starcatcher extends JavaPlugin
{
    public static ComponentType<EntityStore, BobberComponent> bobberComponent;
    private final Config<StarcatcherConfig> config;

    public Starcatcher(@Nonnull JavaPluginInit init)
    {
        super(init);
        this.config = this.withConfig("StarcatcherConfig", StarcatcherConfig.CODEC);
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
        StarcatcherConfig config = this.config.get();
    }
}