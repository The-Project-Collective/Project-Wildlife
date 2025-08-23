package com.collective.projectwildlife.groups.tags;

import com.collective.projectwildlife.ProjectWildlife;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class WildlifeTags {

    // === DIETS ===

    public static final TagKey<Item> AMERICAN_RED_FOX_FOODS = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProjectWildlife.MOD_ID, "diets/specific/fox/american_red_fox_foods"));



    // === ENRICHMENT ===

    // Blocks
    public static final TagKey<Block> AMERICAN_RED_FOX_ENRICHMENT = TagKey.of(RegistryKeys.BLOCK, Identifier.of(ProjectWildlife.MOD_ID, "enrichment/specific/fox/american_red_fox_enrichment"));

    // Entities
    public static final TagKey<EntityType<?>> AMERICAN_RED_FOX_ENRICHMENT_ENTITIES = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ProjectWildlife.MOD_ID, "enrichment/specific/fox/american_red_fox_enrichment_entities"));



    // === ITEMS ===

    public static final TagKey<Item> INSECTS = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProjectWildlife.MOD_ID, "insects"));
}
