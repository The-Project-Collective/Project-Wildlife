package com.collective.projectwildlife.blocks.insects;

import com.collective.projectwildlife.blockentities.WildlifeBlockEntities;
import com.collective.projectwildlife.blockentities.insects.MoleCricketNestBlockEntity;
import com.collective.projectwildlife.blocks.InsectNestBlock;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MoleCricketNestBlock extends InsectNestBlock {

    public static final MapCodec<MoleCricketNestBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(createSettingsCodec()).apply(instance, MoleCricketNestBlock::new));

    public MoleCricketNestBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient()) {
            return null;
        } else {
            return (lvl, pos, blockState, t) -> {
                if (t instanceof MoleCricketNestBlockEntity blockEntity) {
                    blockEntity.tick();
                }
            };
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MoleCricketNestBlockEntity nestEntity) {
                ItemScatterer.spawn(world, pos, nestEntity);
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return WildlifeBlockEntities.MOLE_CRICKET_NEST_ENTITY.get().instantiate(pos, state);
    }
}
