package com.collective.projectwildlife.entities;

import com.collective.projectwildlife.ProjectWildlife;
import com.collective.projectwildlife.entities.animals.mammals.fox.AmericanRedFoxEntity;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class WildlifeEntities {

    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ProjectWildlife.MOD_ID, RegistryKeys.ENTITY_TYPE);

    // === ENTITIES ===

    // Animals
    public static final RegistrySupplier<EntityType<AmericanRedFoxEntity>> AMERICAN_RED_FOX_ENTITY = ENTITIES.register("american_red_fox",
            () -> EntityType.Builder.create(AmericanRedFoxEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.45f, 0.55f)
                    .maxTrackingRange(8)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ProjectWildlife.MOD_ID, "american_red_fox")))
    );


    // === ATTRIBUTES ===

    public static void initializeEntities() {
        EntityAttributeRegistry.register(AMERICAN_RED_FOX_ENTITY::value, AmericanRedFoxEntity::createAttributes);
    }
}
