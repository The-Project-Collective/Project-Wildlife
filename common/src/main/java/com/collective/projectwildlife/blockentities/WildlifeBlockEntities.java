package com.collective.projectwildlife.blockentities;

import com.collective.projectwildlife.ProjectWildlife;
import com.collective.projectwildlife.blockentities.insects.*;
import com.collective.projectwildlife.blocks.WildlifeBlocks;
import com.collective.projectwildlife.util.builders.BlockEntityTypeBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.RegistryKeys;

public class WildlifeBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ProjectWildlife.MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);

    // Insect Nests
    public static final RegistrySupplier<BlockEntityType<CockroachNestBlockEntity>> COCKROACH_NEST_ENTITY = BLOCK_ENTITIES.register("cockroach_nest", () -> BlockEntityTypeBuilder.create(CockroachNestBlockEntity::new, WildlifeBlocks.COCKROACH_NEST.get()));
    public static final RegistrySupplier<BlockEntityType<GrubNestBlockEntity>> GRUB_NEST_ENTITY = BLOCK_ENTITIES.register("grub_nest", () -> BlockEntityTypeBuilder.create(GrubNestBlockEntity::new, WildlifeBlocks.GRUB_NEST.get()));
    public static final RegistrySupplier<BlockEntityType<MealwormNestBlockEntity>> MEALWORM_NEST_ENTITY = BLOCK_ENTITIES.register("mealworm_nest", () -> BlockEntityTypeBuilder.create(MealwormNestBlockEntity::new, WildlifeBlocks.MEALWORM_NEST.get()));
    public static final RegistrySupplier<BlockEntityType<MoleCricketNestBlockEntity>> MOLE_CRICKET_NEST_ENTITY = BLOCK_ENTITIES.register("mole_cricket_nest", () -> BlockEntityTypeBuilder.create(MoleCricketNestBlockEntity::new, WildlifeBlocks.MOLE_CRICKET_NEST.get()));
    public static final RegistrySupplier<BlockEntityType<TermiteNestBlockEntity>> TERMITE_NEST_ENTITY = BLOCK_ENTITIES.register("termite_nest", () -> BlockEntityTypeBuilder.create(TermiteNestBlockEntity::new, WildlifeBlocks.TERMITE_NEST.get()));

}
