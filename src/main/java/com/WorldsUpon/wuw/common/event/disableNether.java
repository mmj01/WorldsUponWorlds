package com.WorldsUpon.wuw.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;



@EventBusSubscriber(modid = "wuw", bus = EventBusSubscriber.Bus.GAME)
public class disableNether {

    @SubscribeEvent
    public static void onPortalSpawn(BlockEvent.PortalSpawnEvent event) {
        event.setCanceled(true);
    }
}
