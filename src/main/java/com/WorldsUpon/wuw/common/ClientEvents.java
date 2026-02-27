package com.WorldsUpon.wuw.common;

import com.WorldsUpon.wuw.common.init.wuwEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.*;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ClientEvents {

    private static final int CHUNK_RADIUS = 2;
    private static final int VERTICAL_RANGE = 32;
    private static final int FULL_RESCAN_INTERVAL = 200;

    private static final Set<Block> IGNORED_BLOCKS = new HashSet<>();
    static {
        // Basic Terrain
        IGNORED_BLOCKS.add(Blocks.AIR);
        IGNORED_BLOCKS.add(Blocks.CAVE_AIR);
        IGNORED_BLOCKS.add(Blocks.VOID_AIR);
        IGNORED_BLOCKS.add(Blocks.STONE);
        IGNORED_BLOCKS.add(Blocks.DEEPSLATE);
        IGNORED_BLOCKS.add(Blocks.COBBLESTONE);
        IGNORED_BLOCKS.add(Blocks.MOSSY_COBBLESTONE);
        IGNORED_BLOCKS.add(Blocks.BEDROCK);

        // Dirt & variants
        IGNORED_BLOCKS.add(Blocks.DIRT);
        IGNORED_BLOCKS.add(Blocks.GRASS_BLOCK);
        IGNORED_BLOCKS.add(Blocks.COARSE_DIRT);
        IGNORED_BLOCKS.add(Blocks.PODZOL);
        IGNORED_BLOCKS.add(Blocks.ROOTED_DIRT);
        IGNORED_BLOCKS.add(Blocks.MUD);
        IGNORED_BLOCKS.add(Blocks.CLAY);

        // Soft Stones
        IGNORED_BLOCKS.add(Blocks.GRANITE);
        IGNORED_BLOCKS.add(Blocks.DIORITE);
        IGNORED_BLOCKS.add(Blocks.ANDESITE);
        IGNORED_BLOCKS.add(Blocks.TUFF);
        IGNORED_BLOCKS.add(Blocks.CALCITE);
        IGNORED_BLOCKS.add(Blocks.DRIPSTONE_BLOCK);
        IGNORED_BLOCKS.add(Blocks.GRAVEL);
        IGNORED_BLOCKS.add(Blocks.SAND);
        IGNORED_BLOCKS.add(Blocks.RED_SAND);
        IGNORED_BLOCKS.add(Blocks.SANDSTONE);
        IGNORED_BLOCKS.add(Blocks.RED_SANDSTONE);

        // Nether / End
        IGNORED_BLOCKS.add(Blocks.NETHERRACK);
        IGNORED_BLOCKS.add(Blocks.BASALT);
        IGNORED_BLOCKS.add(Blocks.BLACKSTONE);
        IGNORED_BLOCKS.add(Blocks.SOUL_SAND);
        IGNORED_BLOCKS.add(Blocks.SOUL_SOIL);
        IGNORED_BLOCKS.add(Blocks.END_STONE);

        // Liquids
        IGNORED_BLOCKS.add(Blocks.WATER);
        IGNORED_BLOCKS.add(Blocks.LAVA);

        // Foliage
        IGNORED_BLOCKS.add(Blocks.GLOW_LICHEN);
        IGNORED_BLOCKS.add(Blocks.SEAGRASS);
        IGNORED_BLOCKS.add(Blocks.TALL_SEAGRASS);
        IGNORED_BLOCKS.add(Blocks.KELP);
        IGNORED_BLOCKS.add(Blocks.KELP_PLANT);
    }

    private static final Map<Block, Integer> BLOCK_COLORS = new HashMap<>();
    static {
        BLOCK_COLORS.put(Blocks.COAL_ORE, 0x333333);
        BLOCK_COLORS.put(Blocks.DEEPSLATE_COAL_ORE, 0x333333);
        BLOCK_COLORS.put(Blocks.IRON_ORE, 0xD8AF93);
        BLOCK_COLORS.put(Blocks.DEEPSLATE_IRON_ORE, 0xD8AF93);
        BLOCK_COLORS.put(Blocks.GOLD_ORE, 0xFCEE4B);
        BLOCK_COLORS.put(Blocks.DEEPSLATE_GOLD_ORE, 0xFCEE4B);
        BLOCK_COLORS.put(Blocks.NETHER_GOLD_ORE, 0xFCEE4B);
        BLOCK_COLORS.put(Blocks.DIAMOND_ORE, 0x00FFFF);
        BLOCK_COLORS.put(Blocks.DEEPSLATE_DIAMOND_ORE, 0x00FFFF);
        BLOCK_COLORS.put(Blocks.LAPIS_ORE, 0x0000FF);
        BLOCK_COLORS.put(Blocks.DEEPSLATE_LAPIS_ORE, 0x0000FF);
        BLOCK_COLORS.put(Blocks.REDSTONE_ORE, 0xFF0000);
        BLOCK_COLORS.put(Blocks.DEEPSLATE_REDSTONE_ORE, 0xFF0000);
        BLOCK_COLORS.put(Blocks.EMERALD_ORE, 0x00FF00);
        BLOCK_COLORS.put(Blocks.DEEPSLATE_EMERALD_ORE, 0x00FF00);
        BLOCK_COLORS.put(Blocks.COPPER_ORE, 0xE77C56);
        BLOCK_COLORS.put(Blocks.DEEPSLATE_COPPER_ORE, 0xE77C56);
        BLOCK_COLORS.put(Blocks.ANCIENT_DEBRIS, 0x594239);
        BLOCK_COLORS.put(Blocks.AMETHYST_CLUSTER, 0xA678F1);
    }

    private record OreData(BlockPos pos, int color) {}

    private static final Map<Long, List<OreData>> chunkCache = new HashMap<>();
    private static ChunkPos lastPlayerChunk = null;

    private static double lastScanY = 0;

    private static int tickCounter = 0;
    private static boolean needsUpdate = false;
    private static final double VERTICAL_THRESHOLD = 6.0; // Re-scan every 5 blocks

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || player.level() == null) return;

        if (!player.hasEffect(wuwEffects.XRAYEYE)) {
            if (!chunkCache.isEmpty()) {
                chunkCache.clear();
                lastPlayerChunk = null;
            }
            return;
        }

        tickCounter++;
        ChunkPos currentChunk = player.chunkPosition();
        double currentY = player.getY();

        boolean verticalChange = Math.abs(currentY - lastScanY) > VERTICAL_THRESHOLD;

        // Check if we moved to a new chunk
        if (!Objects.equals(lastPlayerChunk, currentChunk)) {
            lastPlayerChunk = currentChunk;
            needsUpdate = true;
        }

        if (verticalChange) {
            needsUpdate = true;
            lastScanY = currentY; // Update scan height
            chunkCache.clear();
        }

        if (needsUpdate || tickCounter >= FULL_RESCAN_INTERVAL) {
            scanChunks(player.level(), currentChunk);
            tickCounter = 0;
            needsUpdate = false;
        }
    }

    private static void scanChunks(Level level, ChunkPos center) {
        Set<Long> validChunks = new HashSet<>();

        int playerY = (int) Minecraft.getInstance().player.getY();
        int minWorldY = level.getMinBuildHeight();
        int maxWorldY = level.getMaxBuildHeight();

        // Calculate section indices
        int minSectionIndex = (Math.max(minWorldY, playerY - VERTICAL_RANGE) - minWorldY) >> 4;
        int maxSectionIndex = (Math.min(maxWorldY, playerY + VERTICAL_RANGE) - minWorldY) >> 4;

        for (int x = -CHUNK_RADIUS; x <= CHUNK_RADIUS; x++) {
            for (int z = -CHUNK_RADIUS; z <= CHUNK_RADIUS; z++) {
                int chunkX = center.x + x;
                int chunkZ = center.z + z;
                long chunkKey = ChunkPos.asLong(chunkX, chunkZ);
                validChunks.add(chunkKey);

                // Skip if already cached (unless we cleared cache above)
                if (chunkCache.containsKey(chunkKey)) continue;

                LevelChunk chunk = level.getChunkSource().getChunk(chunkX, chunkZ, false);
                if (chunk == null) continue;

                List<OreData> oresInChunk = new ArrayList<>();
                LevelChunkSection[] sections = chunk.getSections();

                int start = Math.max(0, minSectionIndex);
                int end = Math.min(sections.length - 1, maxSectionIndex);

                for (int i = start; i <= end; i++) {
                    LevelChunkSection section = sections[i];

                    if (section == null || section.hasOnlyAir()) continue;

                    int sectionY = minWorldY + (i * 16);

                    for (int lx = 0; lx < 16; lx++) {
                        for (int ly = 0; ly < 16; ly++) {
                            for (int lz = 0; lz < 16; lz++) {
                                BlockState state = section.getBlockState(lx, ly, lz);
                                Block block = state.getBlock();

                                if (!IGNORED_BLOCKS.contains(block)) {
                                    int worldX = (chunkX << 4) + lx;
                                    int worldY = sectionY + ly;
                                    int worldZ = (chunkZ << 4) + lz;

                                    if (Math.abs(worldY - playerY) > VERTICAL_RANGE) continue;

                                    int color;

                                    if (BLOCK_COLORS.containsKey(block)) {
                                        color = BLOCK_COLORS.get(block);
                                    }
                                    else {
                                        color = state.getMapColor(level, new BlockPos(worldX, worldY, worldZ)).col;
                                    }
                                    oresInChunk.add(new OreData(new BlockPos(worldX, worldY, worldZ), color));
                                }
                            }
                        }
                    }
                }

                if (!oresInChunk.isEmpty()) {
                    chunkCache.put(chunkKey, oresInChunk);
                }
            }
        }
        chunkCache.keySet().retainAll(validChunks);
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        if (chunkCache.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
        Vec3 cameraPos = event.getCamera().getPosition();

        double camX = cameraPos.x;
        double camY = cameraPos.y;
        double camZ = cameraPos.z;

        net.minecraft.client.renderer.culling.Frustum frustum = event.getFrustum();

        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);

        // 3. DRAW LOOP
        for (List<OreData> ores : chunkCache.values()) {
            for (OreData ore : ores) {
                BlockPos pos = ore.pos;

                // Frustum Culling
                if (frustum != null && !frustum.isVisible(new net.minecraft.world.phys.AABB(
                        pos.getX(), pos.getY(), pos.getZ(),
                        pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1))) {
                    continue;
                }

                double x = pos.getX() - camX;
                double y = pos.getY() - camY;
                double z = pos.getZ() - camZ;

                float r = ((ore.color >> 16) & 0xFF) / 255.0f;
                float g = ((ore.color >> 8) & 0xFF) / 255.0f;
                float b = (ore.color & 0xFF) / 255.0f;

                LevelRenderer.renderLineBox(poseStack, buffer, x, y, z, x + 1, y + 1, z + 1, r, g, b, 1.0F);
            }
        }

        // In 1.21, use 'buildOrThrow()' or 'end()' to get the MeshData
        try {
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        } catch (Exception e) {
            // Sometimes strict buffers fail if empty, ignore or handle
        }

        RenderSystem.enableDepthTest();
    }
}