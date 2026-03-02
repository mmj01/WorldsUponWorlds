package com.WorldsUpon.wuw.common.MobSpawns;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = "wuw", bus = EventBusSubscriber.Bus.MOD)
public class mobEventBus {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(wuwMobs.WUW_ZOMBIE.get(), wuwZombie.createAttributes().build());
        event.put(wuwMobs.WUW_SPIDER.get(), wuwSpider.createAttributes().build());
        event.put(wuwMobs.WUW_ENDERMAN.get(), wuwEnderman.createAttributes().build());
        event.put(wuwMobs.WUW_CREEPER.get(), wuwCreeper.createAttributes().build());
        event.put(wuwMobs.WUW_SKELETON.get(), wuwSkeleton.createAttributes().build());
    }
}