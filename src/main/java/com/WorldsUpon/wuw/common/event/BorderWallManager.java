package com.WorldsUpon.wuw.common.event;

import com.WorldsUpon.wuw.wuwBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks; // Needed for Barriers
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;

@EventBusSubscriber(modid = "wuw", bus = EventBusSubscriber.Bus.GAME)
public class BorderWallManager {

    private static final int LIMIT = 300;
    private static final int CEILING_HEIGHT = 1103;

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        LevelAccessor levelAccessor = event.getLevel();

        if (levelAccessor.isClientSide() || !(levelAccessor instanceof Level level)) return;

        String dim = level.dimension().location().toString();


        boolean isTopSector = dim.equals("wuw:top_sector_a");
        boolean isFiller = dim.equals("wuw:filler_sector_a");
        boolean isEndSector = dim.equals("wuw:end_sector_a");

        if (!isTopSector && !isFiller && !isEndSector) return;

        ChunkAccess chunk = event.getChunk();
        ChunkPos chunkPos = chunk.getPos();

        int chunkMinX = chunkPos.getMinBlockX();
        int chunkMaxX = chunkPos.getMaxBlockX();
        int chunkMinZ = chunkPos.getMinBlockZ();
        int chunkMaxZ = chunkPos.getMaxBlockZ();

        boolean hasWallX = (chunkMinX <= -LIMIT && chunkMaxX >= -LIMIT) || (chunkMinX <= LIMIT && chunkMaxX >= LIMIT);
        boolean hasWallZ = (chunkMinZ <= -LIMIT && chunkMaxZ >= -LIMIT) || (chunkMinZ <= LIMIT && chunkMaxZ >= LIMIT);
        boolean needsWalls = hasWallX || hasWallZ;

        boolean insideBounds = (chunkMaxX >= -LIMIT && chunkMinX <= LIMIT) && (chunkMaxZ >= -LIMIT && chunkMinZ <= LIMIT);
        boolean needsCeiling = isTopSector && insideBounds;


        if (!needsWalls && !needsCeiling) return;

        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight() - 1;


        placeBlocks(chunk, chunkMinX, chunkMinZ, minY, maxY, needsWalls, needsCeiling);
    }

    private static void placeBlocks(ChunkAccess chunk, int startX, int startZ, int minY, int maxY, boolean needsWalls, boolean needsCeiling) {
        BlockState wallBlock = wuwBlocks.SUPERWALL.get().defaultBlockState();
        BlockState barrierBlock = Blocks.BARRIER.defaultBlockState();

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int x = 0; x < 16; x++) {
            int worldX = startX + x;

            boolean isWallX = (worldX == -LIMIT || worldX == LIMIT);
            boolean isInsideX = (worldX >= -LIMIT && worldX <= LIMIT);

            for (int z = 0; z < 16; z++) {
                int worldZ = startZ + z;

                boolean isWallZ = (worldZ == -LIMIT || worldZ == LIMIT);
                boolean isInsideZ = (worldZ >= -LIMIT && worldZ <= LIMIT);
                if (needsWalls) {
                    if ((isWallX && isInsideZ) || (isWallZ && isInsideX)) {
                        for (int y = minY; y <= maxY; y++) {
                            mutablePos.set(worldX, y, worldZ);

                            if (needsCeiling && y == CEILING_HEIGHT) continue;
                            chunk.setBlockState(mutablePos, wallBlock, false);
                        }
                    }
                }
                if (needsCeiling) {
                    if (isInsideX && isInsideZ) {
                        mutablePos.set(worldX, CEILING_HEIGHT, worldZ);
                        chunk.setBlockState(mutablePos, barrierBlock, false);
                    }
                }
            }
        }
        chunk.setUnsaved(true);
    }
}