package com.collective.projectwildlife.groups.tags;

import com.collective.projectcore.ProjectCore;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class WildlifeTags {

    // === GENERAL DIETS ===

    // Diets
    public static final TagKey<Item> AMERICAN_RED_FOX_FOODS = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProjectCore.MOD_ID, "diets/specific/fox/american_red_fox_foods"));
}
