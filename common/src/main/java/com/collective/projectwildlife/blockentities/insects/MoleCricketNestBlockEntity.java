package com.collective.projectwildlife.blockentities.insects;

import com.collective.projectwildlife.blockentities.InsectNestBlockEntity;
import com.collective.projectwildlife.blockentities.WildlifeBlockEntities;
import com.collective.projectwildlife.items.WildlifeItems;
import com.collective.projectwildlife.screens.handlers.insects.MoleCricketNestScreenHandler;
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

public class MoleCricketNestBlockEntity extends InsectNestBlockEntity {

    public MoleCricketNestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(WildlifeBlockEntities.MOLE_CRICKET_NEST_ENTITY.get(), blockPos, blockState);
        this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("screenhandler.project_wildlife.mole_cricket_nest");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new MoleCricketNestScreenHandler(syncId, playerInventory, this);
    }

    public ItemStack getInsect() {
        return new ItemStack(WildlifeItems.MOLE_CRICKET);
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
