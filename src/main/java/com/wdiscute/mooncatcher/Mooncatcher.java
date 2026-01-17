package com.wdiscute.mooncatcher;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.wdiscute.mooncatcher.components.BobberComponent;
import com.wdiscute.mooncatcher.interactions.RodInteraction;
import com.wdiscute.mooncatcher.storage.Fishes;
import com.wdiscute.mooncatcher.systems.BobberSystem;

import javax.annotation.Nonnull;

public class Mooncatcher extends JavaPlugin
{
    public static ComponentType<EntityStore, BobberComponent> bobberComponent;
    private final Config<MooncatcherConfig> config;

    public Mooncatcher(@Nonnull JavaPluginInit init)
    {
        super(init);
        this.config = this.withConfig("MooncatcherConfig", MooncatcherConfig.CODEC);
    }

    @Override
    protected void setup()
    {
        //register fishes
        Fishes.setup();

        //component
        bobberComponent = this.getEntityStoreRegistry().registerComponent(BobberComponent.class, BobberComponent::new);

        //interaction/codec?
        this.getCodecRegistry(Interaction.CODEC).register("MooncatcherCast", RodInteraction.class, RodInteraction.CODEC);

        //system
        this.getEntityStoreRegistry().registerSystem(new BobberSystem());

    }

    @Override
    protected void start()
    {
        super.start();
        this.config.save();
        MooncatcherConfig config = this.config.get();
    }
}