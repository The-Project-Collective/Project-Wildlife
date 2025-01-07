package com.collective.projectwildlife.blockentities.machines;

import com.collective.projectcore.blockentities.CoreFeederBlockEntity;
import com.collective.projectwildlife.blockentities.WildlifeBlockEntities;
import com.collective.projectwildlife.screens.handlers.machines.FeederScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class FeederBlockEntity extends CoreFeederBlockEntity {

    public FeederBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(WildlifeBlockEntities.FEEDER_ENTITY.get(), blockPos, blockState);
    }

    protected Text getContainerName() {
        return Text.translatable("screenhandler.project_wildlife.feeder");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FeederScreenHandler(syncId, playerInventory, this);
    }

}
