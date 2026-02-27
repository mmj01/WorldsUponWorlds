package com.WorldsUpon.wuw.common.event;

import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.bus.api.Event; // This fixes the "Cannot resolve symbol Result" error

// BUS = GAME (Runs every tick)
@EventBusSubscriber(modid = "wuw", bus = EventBusSubscriber.Bus.GAME)
public class SpawnEventHelper {

    @SubscribeEvent
    public static void onPositionCheck(MobSpawnEvent.PositionCheck event) {
        // Only force spawn for Monsters (Zombies, Creepers, etc.)
        // We don't want to force Squid to spawn on land.
        if (event.getEntity() instanceof Monster) {

            // ALLOW forces the spawn, bypassing the vanilla light level check.
            event.setResult(Event.Result.ALLOW);
        }
    }
}