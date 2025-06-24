package com.collective.projectwildlife.fabric.worldgen;

import com.collective.projectwildlife.entities.WildlifeEntities;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class WildlifeEntitySpawnsFabric {

    public static void addSpawns() {
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.PLAINS, BiomeKeys.SUNFLOWER_PLAINS, BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST, BiomeKeys.DARK_FOREST),
                SpawnGroup.CREATURE,
                WildlifeEntities.AMERICAN_RED_FOX_ENTITY.get(),
                30, 1, 4);

        SpawnRestriction.register(
                WildlifeEntities.AMERICAN_RED_FOX_ENTITY.get(),
                SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                AnimalEntity::isValidNaturalSpawn);
    }
}
