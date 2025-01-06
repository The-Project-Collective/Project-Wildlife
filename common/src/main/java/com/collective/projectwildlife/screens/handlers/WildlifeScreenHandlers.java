package com.collective.projectwildlife.screens.handlers;

import com.collective.projectwildlife.ProjectWildlife;
import com.collective.projectwildlife.screens.handlers.insects.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class WildlifeScreenHandlers {

    public static final DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLERS = DeferredRegister.create(ProjectWildlife.MOD_ID, RegistryKeys.SCREEN_HANDLER);

    // === BLOCKS ===

    // Insect Nests
    public static final RegistrySupplier<ScreenHandlerType<CockroachNestScreenHandler>> COCKROACH_NEST_SCREEN_HANDLER =
            SCREEN_HANDLERS.register("cockroach_nest", () -> new ScreenHandlerType<>(CockroachNestScreenHandler::new, FeatureFlags.DEFAULT_ENABLED_FEATURES));
    public static final RegistrySupplier<ScreenHandlerType<GrubNestScreenHandler>> GRUB_NEST_SCREEN_HANDLER =
            SCREEN_HANDLERS.register("grub_nest", () -> new ScreenHandlerType<>(GrubNestScreenHandler::new, FeatureFlags.DEFAULT_ENABLED_FEATURES));
    public static final RegistrySupplier<ScreenHandlerType<MealwormNestScreenHandler>> MEALWORM_NEST_SCREEN_HANDLER =
            SCREEN_HANDLERS.register("mealworm_nest", () -> new ScreenHandlerType<>(MealwormNestScreenHandler::new, FeatureFlags.DEFAULT_ENABLED_FEATURES));
    public static final RegistrySupplier<ScreenHandlerType<MoleCricketNestScreenHandler>> MOLE_CRICKET_NEST_SCREEN_HANDLER =
            SCREEN_HANDLERS.register("mole_cricket_nest", () -> new ScreenHandlerType<>(MoleCricketNestScreenHandler::new, FeatureFlags.DEFAULT_ENABLED_FEATURES));
    public static final RegistrySupplier<ScreenHandlerType<TermiteNestScreenHandler>> TERMITE_NEST_SCREEN_HANDLER =
            SCREEN_HANDLERS.register("termite_nest", () -> new ScreenHandlerType<>(TermiteNestScreenHandler::new, FeatureFlags.DEFAULT_ENABLED_FEATURES));
}
