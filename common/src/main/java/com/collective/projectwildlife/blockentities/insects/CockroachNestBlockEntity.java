package com.collective.projectwildlife.blockentities.insects;

import com.collective.projectwildlife.blockentities.InsectNestBlockEntity;
import com.collective.projectwildlife.blockentities.WildlifeBlockEntities;
import com.collective.projectwildlife.items.WildlifeItems;
import com.collective.projectwildlife.screens.handlers.insects.CockroachNestScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class CockroachNestBlockEntity extends InsectNestBlockEntity {

    public CockroachNestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(WildlifeBlockEntities.COCKROACH_NEST_ENTITY.get(), blockPos, blockState);
        this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("screenhandler.project_wildlife.cockroach_nest");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CockroachNestScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public ItemStack getInsect() {
        return new ItemStack(WildlifeItems.COCKROACH);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
    }
}
