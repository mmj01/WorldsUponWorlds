package com.WorldsUpon.wuw.common.MobSpawns;

import net.minecraft.client.renderer.entity.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class mobEvents {
    @EventBusSubscriber(modid = "wuw", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(wuwMobs.WUW_ZOMBIE.get(), ZombieRenderer::new);
            event.registerEntityRenderer(wuwMobs.WUW_SKELETON.get(), SkeletonRenderer::new);
            event.registerEntityRenderer(wuwMobs.WUW_ENDERMAN.get(), EndermanRenderer::new);
            event.registerEntityRenderer(wuwMobs.WUW_CREEPER.get(), CreeperRenderer::new);
            event.registerEntityRenderer(wuwMobs.WUW_SPIDER.get(), SpiderRenderer::new);
        }
    }

    @EventBusSubscriber(modid = "wuw", bus = EventBusSubscriber.Bus.MOD)
    public static class CommonEvents {
        @SubscribeEvent
        public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
            registerMyMob(event, wuwMobs.WUW_ZOMBIE.get());
            registerMyMob(event, wuwMobs.WUW_SPIDER.get());
            registerMyMob(event, wuwMobs.WUW_SKELETON.get());
            registerMyMob(event, wuwMobs.WUW_ENDERMAN.get());
            registerMyMob(event, wuwMobs.WUW_CREEPER.get());
        }

        private static void registerMyMob(RegisterSpawnPlacementsEvent event, net.minecraft.world.entity.EntityType<?> type) {
            event.register(
                    type,
                    SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, level, spawnType, pos, random) -> !level.getBlockState(pos.below()).is(Blocks.STONE),
                    RegisterSpawnPlacementsEvent.Operation.REPLACE
            );
        }
    }
}