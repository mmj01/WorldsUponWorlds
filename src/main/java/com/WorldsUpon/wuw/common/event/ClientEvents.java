package com.WorldsUpon.wuw.common.event;

import com.WorldsUpon.wuw.common.block.wuwBlocks;
import com.WorldsUpon.wuw.common.init.wuwEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;
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

    private static final Map<ResourceLocation, Integer> BLOCK_COLORS = new HashMap<>();

    private static void initBlockColors() {
        if (!BLOCK_COLORS.isEmpty()) return;

        BLOCK_COLORS.put(BuiltInRegistries.BLOCK.getKey(wuwBlocks.COAL_ORE.get()), 0x333333);
        BLOCK_COLORS.put(BuiltInRegistries.BLOCK.getKey(wuwBlocks.IRON_ORE.get()), 0xD8AF93);
        BLOCK_COLORS.put(BuiltInRegistries.BLOCK.getKey(wuwBlocks.HCP_IRON_ORE.get()), 0x594239);
        BLOCK_COLORS.put(BuiltInRegistries.BLOCK.getKey(wuwBlocks.GOLD_ORE.get()), 0xFCEE4B);
        BLOCK_COLORS.put(BuiltInRegistries.BLOCK.getKey(wuwBlocks.DIAMOND_ORE.get()), 0x00FFFF);
        BLOCK_COLORS.put(BuiltInRegistries.BLOCK.getKey(wuwBlocks.LAPIS_ORE.get()), 0x0000FF);
        BLOCK_COLORS.put(BuiltInRegistries.BLOCK.getKey(wuwBlocks.REDSTONE_ORE.get()), 0xFF0000);
        BLOCK_COLORS.put(BuiltInRegistries.BLOCK.getKey(wuwBlocks.EMERALD_ORE.get()), 0x00FF00);
        BLOCK_COLORS.put(BuiltInRegistries.BLOCK.getKey(wuwBlocks.COPPER_ORE.get()), 0xE77C56);
        BLOCK_COLORS.put(BuiltInRegistries.BLOCK.getKey(Blocks.ANCIENT_DEBRIS), 0x594239);
        BLOCK_COLORS.put(BuiltInRegistries.BLOCK.getKey(Blocks.AMETHYST_BLOCK), 0xA678F1);
    }

    private record OreData(BlockPos pos, int color) {}

    private static final Map<Long, List<OreData>> chunkCache = new HashMap<>();
    private static ChunkPos lastPlayerChunk = null;
    private static double lastScanY = 0;
    private static int tickCounter = 0;
    private static boolean needsUpdate = false;
    private static final double VERTICAL_THRESHOLD = 6.0;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        initBlockColors();

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

        if (!Objects.equals(lastPlayerChunk, currentChunk)) {
            lastPlayerChunk = currentChunk;
            needsUpdate = true;
        }

        if (verticalChange) {
            needsUpdate = true;
            lastScanY = currentY;
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

        for (int x = -CHUNK_RADIUS; x <= CHUNK_RADIUS; x++) {
            for (int z = -CHUNK_RADIUS; z <= CHUNK_RADIUS; z++) {
                int chunkX = center.x + x;
                int chunkZ = center.z + z;
                long chunkKey = ChunkPos.asLong(chunkX, chunkZ);
                validChunks.add(chunkKey);

                if (chunkCache.containsKey(chunkKey)) continue;

                LevelChunk chunk = level.getChunk(chunkX, chunkZ);
                if (chunk == null) continue;

                List<OreData> oresInChunk = new ArrayList<>();
                LevelChunkSection[] sections = chunk.getSections();

                for (int i = 0; i < sections.length; i++) {
                    LevelChunkSection section = sections[i];
                    if (section == null || section.hasOnlyAir()) continue;

                    int sectionY = minWorldY + (i * 16);

                    if (Math.abs(sectionY - playerY) > VERTICAL_RANGE + 16) continue;

                    for (int lx = 0; lx < 16; lx++) {
                        for (int ly = 0; ly < 16; ly++) {
                            for (int lz = 0; lz < 16; lz++) {
                                BlockState state = section.getBlockState(lx, ly, lz);
                                Block block = state.getBlock();
                                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);

                                if (!BLOCK_COLORS.containsKey(blockId)) {
                                    continue;
                                }

                                int worldY = sectionY + ly;
                                if (Math.abs(worldY - playerY) > VERTICAL_RANGE) continue;

                                int worldX = (chunkX << 4) + lx;
                                int worldZ = (chunkZ << 4) + lz;
                                int color = BLOCK_COLORS.get(blockId);
                                oresInChunk.add(new OreData(new BlockPos(worldX, worldY, worldZ), color));
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

        PoseStack poseStack = event.getPoseStack();
        Vec3 cameraPos = event.getCamera().getPosition();
        net.minecraft.client.renderer.culling.Frustum frustum = event.getFrustum();

        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);

        for (List<OreData> ores : chunkCache.values()) {
            for (OreData ore : ores) {
                BlockPos pos = ore.pos;
                AABB aabb = new AABB(pos);

                if (frustum != null && !frustum.isVisible(aabb)) {
                    continue;
                }

                float r = ((ore.color >> 16) & 0xFF) / 255.0f;
                float g = ((ore.color >> 8) & 0xFF) / 255.0f;
                float b = (ore.color & 0xFF) / 255.0f;

                LevelRenderer.renderLineBox(
                        poseStack,
                        bufferbuilder,
                        aabb.minX - cameraPos.x, aabb.minY - cameraPos.y, aabb.minZ - cameraPos.z,
                        aabb.maxX - cameraPos.x, aabb.maxY - cameraPos.y, aabb.maxZ - cameraPos.z,
                        r, g, b, 1.0F
                );
            }
        }

        try {
            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        } catch (Exception e) {
        }

        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}