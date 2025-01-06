package com.collective.projectwildlife.fabric.client;

import com.collective.projectwildlife.screens.handlers.WildlifeScreenHandlers;
import com.collective.projectwildlife.screens.insects.*;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.ClientModInitializer;

public final class ProjectWildlifeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerEntityRenderers();
        registerBlockRenderers();
        registerScreens();
    }

    public static void registerEntityRenderers() {

    }

    public static void registerBlockRenderers() {

    }

    public static void registerScreens() {
        if (WildlifeScreenHandlers.SCREEN_HANDLERS.getRegistrar() != null) {
            if (WildlifeScreenHandlers.COCKROACH_NEST_SCREEN_HANDLER.isPresent()) {
                MenuRegistry.registerScreenFactory(WildlifeScreenHandlers.COCKROACH_NEST_SCREEN_HANDLER.get(), CockroachNestScreen::new);
            }
            if (WildlifeScreenHandlers.GRUB_NEST_SCREEN_HANDLER.isPresent()) {
                MenuRegistry.registerScreenFactory(WildlifeScreenHandlers.GRUB_NEST_SCREEN_HANDLER.get(), GrubNestScreen::new);
            }
            if (WildlifeScreenHandlers.MEALWORM_NEST_SCREEN_HANDLER.isPresent()) {
                MenuRegistry.registerScreenFactory(WildlifeScreenHandlers.MEALWORM_NEST_SCREEN_HANDLER.get(), MealwormNestScreen::new);
            }
            if (WildlifeScreenHandlers.MOLE_CRICKET_NEST_SCREEN_HANDLER.isPresent()) {
                MenuRegistry.registerScreenFactory(WildlifeScreenHandlers.MOLE_CRICKET_NEST_SCREEN_HANDLER.get(), MoleCricketNestScreen::new);
            }
            if (WildlifeScreenHandlers.TERMITE_NEST_SCREEN_HANDLER.isPresent()) {
                MenuRegistry.registerScreenFactory(WildlifeScreenHandlers.TERMITE_NEST_SCREEN_HANDLER.get(), TermiteNestScreen::new);
            }
        }
    }
}
