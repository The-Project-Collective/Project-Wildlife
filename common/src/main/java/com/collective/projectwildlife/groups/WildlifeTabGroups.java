package com.collective.projectwildlife.groups;

import com.collective.projectcore.ProjectCore;
import com.collective.projectwildlife.items.WildlifeItems;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public class WildlifeTabGroups {

    public static final DeferredRegister<ItemGroup> TAB_GROUPS = DeferredRegister.create(ProjectCore.MOD_ID, RegistryKeys.ITEM_GROUP);

    public static final RegistrySupplier<ItemGroup> WILDLIFE_ANIMALS = TAB_GROUPS.register("wildlife_animals", () -> CreativeTabRegistry
            .create(Text.translatable("group.project_wildlife.wildlife_animals"), () -> new ItemStack(WildlifeItems.AMERICAN_RED_FOX_SPAWN_EGG.get())));

}
