package com.collective.projectwildlife;

import com.collective.projectwildlife.blockentities.WildlifeBlockEntities;
import com.collective.projectwildlife.blocks.WildlifeBlocks;
import com.collective.projectwildlife.entities.WildlifeEntities;
import com.collective.projectwildlife.groups.WildlifeTabGroups;
import com.collective.projectwildlife.items.WildlifeItems;
import com.collective.projectwildlife.screens.handlers.WildlifeScreenHandlers;

public final class ProjectWildlife {
    public static final String MOD_ID = "project_wildlife";

    public static void init() {
        WildlifeBlocks.BLOCKS.register();
        WildlifeBlocks.BLOCK_ITEMS.register();
        WildlifeItems.ITEMS.register();
        WildlifeBlockEntities.BLOCK_ENTITIES.register();
        WildlifeEntities.ENTITIES.register();
        WildlifeEntities.initializeEntities();
        WildlifeTabGroups.TAB_GROUPS.register();
        WildlifeScreenHandlers.SCREEN_HANDLERS.register();
    }
}
