package com.WorldsUpon.wuw.common.event;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

@EventBusSubscriber(modid = "wuw", bus = EventBusSubscriber.Bus.MOD)
public class wuwMobSpawns {

    // 1. Keep your existing RegisterSpawnPlacementsEvent (It helps find valid ground)
    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (type.getCategory() == MobCategory.MONSTER || type.getCategory() == MobCategory.CREATURE) {
                try {
                    register(event, type);
                } catch (Exception e) {
                }
            }
        }
    }

    // 2. NEW: The "Force Spawn" Event
    // This runs every time the game thinks about spawning a mob.
    // We set it to ALLOW to bypass the native light level checks that the Spawner does.
    @SubscribeEvent
    public static void onPositionCheck(MobSpawnEvent.PositionCheck event) {
        // Only apply to Monsters (Zombies, Creepers, etc.)
        if (event.getEntity() instanceof Monster) {

            // Optional: Limit this to your specific dimension if you want
            // if (event.getLevel().dimension() == Level.OVERWORLD) { ... }

            // FORCE the spawn to happen, ignoring light levels
            event.setResult(net.neoforged.bus.api.Event.Result.ALLOW);
        }
    }

    // Helper method for the Register Event
    private static <T extends Mob> void register(RegisterSpawnPlacementsEvent event, EntityType<?> rawType) {
        @SuppressWarnings("unchecked")
        EntityType<T> type = (EntityType<T>) rawType;

        event.register(
                type,
                SpawnPlacementTypes.ON_GROUND,
                // CHANGE THIS to MOTION_BLOCKING_NO_LEAVES if you want them under the trees!
                // MOTION_BLOCKING puts them ON TOP of the leaves (in the sun).
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (pEntity, pLevel, pSpawnType, pPos, pRandom) -> {
                    BlockState blockBelow = pLevel.getBlockState(pPos.below());
                    return !blockBelow.is(Blocks.STONE) && !blockBelow.is(Blocks.DEEPSLATE);
                },
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }
}