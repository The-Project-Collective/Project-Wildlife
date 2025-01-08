package com.collective.projectwildlife.neoforge.client;

import com.collective.projectwildlife.ProjectWildlife;
import com.collective.projectwildlife.entities.WildlifeEntities;
import com.collective.projectwildlife.renderers.entities.animals.mammals.fox.AmericanRedFoxRenderer;
import com.collective.projectwildlife.screens.handlers.WildlifeScreenHandlers;
import com.collective.projectwildlife.screens.insects.*;
import com.collective.projectwildlife.screens.machines.FeederScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = ProjectWildlife.MOD_ID, dist = Dist.CLIENT)
public class ProjectWildlifeNeoForgeClient {

    public ProjectWildlifeNeoForgeClient(IEventBus modBus) {
        modBus.register(this);
    }

    @SubscribeEvent
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(WildlifeEntities.AMERICAN_RED_FOX_ENTITY.get(), AmericanRedFoxRenderer::new);

    }

    @SubscribeEvent
    public void registerBlockRenderers(final EntityRenderersEvent.RegisterRenderers event) {

    }

    @SubscribeEvent
    public void registerMenuScreensEvent(RegisterMenuScreensEvent event) {
        event.register(WildlifeScreenHandlers.COCKROACH_NEST_SCREEN_HANDLER.get(), CockroachNestScreen::new);
        event.register(WildlifeScreenHandlers.GRUB_NEST_SCREEN_HANDLER.get(), GrubNestScreen::new);
        event.register(WildlifeScreenHandlers.MEALWORM_NEST_SCREEN_HANDLER.get(), MealwormNestScreen::new);
        event.register(WildlifeScreenHandlers.MOLE_CRICKET_NEST_SCREEN_HANDLER.get(), MoleCricketNestScreen::new);
        event.register(WildlifeScreenHandlers.TERMITE_NEST_SCREEN_HANDLER.get(), TermiteNestScreen::new);

        event.register(WildlifeScreenHandlers.FEEDER_SCREEN_HANDLER.get(), FeederScreen::new);

    }

}
