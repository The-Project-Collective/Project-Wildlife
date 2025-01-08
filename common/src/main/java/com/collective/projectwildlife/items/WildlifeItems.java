package com.collective.projectwildlife.items;

import com.collective.projectcore.groups.CoreTabGroups;
import com.collective.projectcore.items.CoreSpawnEggItem;
import com.collective.projectwildlife.ProjectWildlife;
import com.collective.projectwildlife.entities.WildlifeEntities;
import com.collective.projectwildlife.groups.WildlifeTabGroups;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@SuppressWarnings("UnstableApiUsage") // arch$tab seems to cause this?
public class WildlifeItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ProjectWildlife.MOD_ID, RegistryKeys.ITEM);

    // === FOOD ITEMS =======================================================================================================================================================================

    // --- Insects ------------------------------------------------------------------------------------------
    public static final RegistrySupplier<Item> COCKROACH = ITEMS.register("cockroach", () -> new Item(new Item.Settings().maxCount(64).arch$tab(CoreTabGroups.CORE_ITEMS).registryKey(getItemRegistryKey("cockroach"))));
    public static final RegistrySupplier<Item> GRUB = ITEMS.register("grub", () -> new Item(new Item.Settings().maxCount(64).arch$tab(CoreTabGroups.CORE_ITEMS).registryKey(getItemRegistryKey("grub"))));
    public static final RegistrySupplier<Item> MEALWORM = ITEMS.register("mealworm", () -> new Item(new Item.Settings().maxCount(64).arch$tab(CoreTabGroups.CORE_ITEMS).registryKey(getItemRegistryKey("mealworm"))));
    public static final RegistrySupplier<Item> MOLE_CRICKET = ITEMS.register("mole_cricket", () -> new Item(new Item.Settings().maxCount(64).arch$tab(CoreTabGroups.CORE_ITEMS).registryKey(getItemRegistryKey("mole_cricket"))));
    public static final RegistrySupplier<Item> TERMITE = ITEMS.register("termite", () -> new Item(new Item.Settings().maxCount(64).arch$tab(CoreTabGroups.CORE_ITEMS).registryKey(getItemRegistryKey("termite"))));



    // === SPAWN EGGS =======================================================================================================================================================================

    // --- Fox ------------------------------------------------------------------------------------------
    public static final RegistrySupplier<Item> AMERICAN_RED_FOX_SPAWN_EGG = ITEMS.register("american_red_fox_spawn_egg", () -> new CoreSpawnEggItem(WildlifeEntities.AMERICAN_RED_FOX_ENTITY, Text.translatable("scientific.project_wildlife.american_red_fox"), new Item.Settings().maxCount(64).arch$tab(WildlifeTabGroups.WILDLIFE_ANIMALS).registryKey(getItemRegistryKey("american_red_fox_spawn_egg"))));



    // === HELPER METHODS ===

    private static RegistryKey<Item> getItemRegistryKey(String item_id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ProjectWildlife.MOD_ID, item_id));
    }
}
