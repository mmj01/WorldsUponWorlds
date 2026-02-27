package com.WorldsUpon.wuw.common.block;

import com.WorldsUpon.wuw.wuwDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Set;

public class floorbasic extends Block {


    public floorbasic(Properties properties) {
        super(properties);
    }

    private void teleportPlayer(Level level, BlockPos pos, Player player) {
        double targetX = pos.getX();
        double targetY = 0;
        double targetZ = pos.getZ();
        String dimName="";
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {

            ResourceKey<Level> currentDimensionKey = level.dimension();

            ResourceKey<Level> targetDimensionKey = null;

            if (currentDimensionKey == wuwDimensions.TOP_SECTOR_A) {
                targetDimensionKey = wuwDimensions.FILLER_SECTOR_A;
                targetY = 1020;
                dimName="Mid-Sector 01";
            } else if (currentDimensionKey == wuwDimensions.FILLER_SECTOR_A) {
                if (pos.getY() == 1023) {
                    targetDimensionKey = wuwDimensions.TOP_SECTOR_A;
                    targetY = -1022;
                    dimName="Top-Sector 01";
                } else {
                    targetDimensionKey = wuwDimensions.END_SECTOR_A;
                    targetY = 1020;
                    dimName="End-Sector 01";
                }

            } else if (currentDimensionKey == wuwDimensions.END_SECTOR_A) {
                targetDimensionKey = wuwDimensions.FILLER_SECTOR_A;
                targetY = -1023;
                dimName="Mid-Sector 01";
                ServerLevel targetLevel = serverPlayer.server.getLevel(targetDimensionKey);
            }

            if (targetDimensionKey != null) {
                ServerLevel targetLevel = serverPlayer.server.getLevel(targetDimensionKey);

                if (targetLevel != null) {
                    serverPlayer.teleportTo(
                            targetLevel,
                            targetX,
                            targetY,
                            targetZ,
                            serverPlayer.getYRot(),
                            serverPlayer.getXRot()
                    );
                }


                serverPlayer.connection.send(new ClientboundSetTitlesAnimationPacket(10, 100, 20));

                serverPlayer.connection.send(new ClientboundSetTitleTextPacket(Component.empty()));

                serverPlayer.connection.send(new ClientboundSetSubtitleTextPacket(
                        Component.literal("Entering " + dimName)));
            }
        }
    }
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        teleportPlayer(level, pos, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        teleportPlayer(level, pos, player);
        return ItemInteractionResult.SUCCESS;
    }
}