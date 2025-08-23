package com.collective.projectwildlife.neoforge.events;

import com.collective.projectwildlife.ProjectWildlife;
import com.collective.projectwildlife.entities.WildlifeEntities;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = ProjectWildlife.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class WildlifeEventBusNeoForge {

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(WildlifeEntities.AMERICAN_RED_FOX_ENTITY.get(), SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                AnimalEntity::canMobSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
