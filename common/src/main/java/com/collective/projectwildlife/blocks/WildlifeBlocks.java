package com.collective.projectwildlife.blocks;

import com.collective.projectcore.groups.CoreTabGroups;
import com.collective.projectwildlife.ProjectWildlife;
import com.collective.projectwildlife.blocks.insects.*;
import com.collective.projectwildlife.blocks.machines.FeederBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class WildlifeBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ProjectWildlife.MOD_ID, RegistryKeys.BLOCK);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ProjectWildlife.MOD_ID, RegistryKeys.ITEM);

    // === UTILITY / MACHINES ===

    public static final RegistrySupplier<Block> FEEDER = registerBlock("feeder", () -> new FeederBlock(AbstractBlock.Settings.copy(Blocks.STONE).mapColor(MapColor.BROWN).requiresTool().registryKey(getBlockRegistryKey("feeder"))));



    // === WORLD GEN ===

    // Insect Nests
    public static final RegistrySupplier<Block> WILD_COCKROACH_NEST = registerBlock("wild_cockroach_nest", () -> new Block(AbstractBlock.Settings.copy(Blocks.COARSE_DIRT).mapColor(MapColor.DIRT_BROWN).registryKey(getBlockRegistryKey("wild_cockroach_nest"))));
    public static final RegistrySupplier<Block> WILD_GRUB_NEST = registerBlock("wild_grub_nest", () -> new Block(AbstractBlock.Settings.copy(Blocks.COARSE_DIRT).mapColor(MapColor.DIRT_BROWN).registryKey(getBlockRegistryKey("wild_grub_nest"))));
    public static final RegistrySupplier<Block> WILD_MEALWORM_NEST = registerBlock("wild_mealworm_nest", () -> new Block(AbstractBlock.Settings.copy(Blocks.COARSE_DIRT).mapColor(MapColor.DIRT_BROWN).registryKey(getBlockRegistryKey("wild_mealworm_nest"))));
    public static final RegistrySupplier<Block> WILD_MOLE_CRICKET_NEST = registerBlock("wild_mole_cricket_nest", () -> new Block(AbstractBlock.Settings.copy(Blocks.COARSE_DIRT).mapColor(MapColor.DIRT_BROWN).registryKey(getBlockRegistryKey("wild_mole_cricket_nest"))));
    public static final RegistrySupplier<Block> WILD_TERMITE_NEST = registerBlock("wild_termite_nest", () -> new Block(AbstractBlock.Settings.copy(Blocks.COARSE_DIRT).mapColor(MapColor.DIRT_BROWN).registryKey(getBlockRegistryKey("wild_termite_nest"))));

    public static final RegistrySupplier<Block> COCKROACH_NEST = registerBlock("cockroach_nest", () -> new CockroachNestBlock(AbstractBlock.Settings.copy(Blocks.COARSE_DIRT).mapColor(MapColor.DIRT_BROWN).registryKey(getBlockRegistryKey("cockroach_nest"))));
    public static final RegistrySupplier<Block> GRUB_NEST = registerBlock("grub_nest", () -> new GrubNestBlock(AbstractBlock.Settings.copy(Blocks.COARSE_DIRT).mapColor(MapColor.DIRT_BROWN).registryKey(getBlockRegistryKey("grub_nest"))));
    public static final RegistrySupplier<Block> MEALWORM_NEST = registerBlock("mealworm_nest", () -> new MealwormNestBlock(AbstractBlock.Settings.copy(Blocks.COARSE_DIRT).mapColor(MapColor.DIRT_BROWN).registryKey(getBlockRegistryKey("mealworm_nest"))));
    public static final RegistrySupplier<Block> MOLE_CRICKET_NEST = registerBlock("mole_cricket_nest", () -> new MoleCricketNestBlock(AbstractBlock.Settings.copy(Blocks.COARSE_DIRT).mapColor(MapColor.DIRT_BROWN).registryKey(getBlockRegistryKey("mole_cricket_nest"))));
    public static final RegistrySupplier<Block> TERMITE_NEST = registerBlock("termite_nest", () -> new TermiteNestBlock(AbstractBlock.Settings.copy(Blocks.COARSE_DIRT).mapColor(MapColor.DIRT_BROWN).registryKey(getBlockRegistryKey("termite_nest"))));



    // === HELPER METHODS ===

    private static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> block) {
        RegistrySupplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static <T extends Block> void registerBlockItem(String name, RegistrySupplier<T> block) {
        BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Settings().arch$tab(CoreTabGroups.CORE_BLOCKS).registryKey(getItemRegistryKey(name)).useBlockPrefixedTranslationKey()));
    }

    private static RegistryKey<Block> getBlockRegistryKey(String block_id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(ProjectWildlife.MOD_ID, block_id));
    }

    private static RegistryKey<Item> getItemRegistryKey(String item_id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ProjectWildlife.MOD_ID, item_id));
    }
}
