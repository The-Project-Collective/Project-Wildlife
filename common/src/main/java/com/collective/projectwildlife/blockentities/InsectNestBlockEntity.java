package com.collective.projectwildlife.blockentities;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public abstract class InsectNestBlockEntity extends LockableContainerBlockEntity implements SidedInventory, ExtendedMenuProvider {

    public static final int INVENTORY_SIZE = 1;
    public DefaultedList<ItemStack> inventory;

    private final int SPAWN_TICKS = 600;
    private int ticks = 0;

    public InsectNestBlockEntity(BlockEntityType<?> nestBlockEntity, BlockPos blockPos, BlockState blockState) {
        super(nestBlockEntity, blockPos, blockState);
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    public void tick() {
        if (ticks >= SPAWN_TICKS) {
            ItemStack stack = this.getStack(0);
            if (stack.getCount() < stack.getMaxCount()) {
                if (!stack.isEmpty()) {
                    stack.setCount(stack.getCount() + 1);
                } else {
                    this.inventory.set(0, getInsect());
                }
                ticks = 0;
                markDirty();
            }
        } else {
            ticks++;
        }
    }

    @Override
    public void markDirty() {
        if (world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
        super.markDirty();
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
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

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[] {0};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public void saveExtraData(PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    // OVERRIDES

    @Override
    protected abstract Text getContainerName();

    @Override
    protected abstract ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory);

    public abstract ItemStack getInsect();

}
