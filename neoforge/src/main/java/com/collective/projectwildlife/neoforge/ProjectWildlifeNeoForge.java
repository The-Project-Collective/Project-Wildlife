package com.collective.projectwildlife.neoforge;

import net.neoforged.fml.common.Mod;

import com.collective.projectwildlife.ProjectWildlife;

@Mod(ProjectWildlife.MOD_ID)
public final class ProjectWildlifeNeoForge {
    public ProjectWildlifeNeoForge() {
        // Run our common setup.
        ProjectWildlife.init();
    }
}
