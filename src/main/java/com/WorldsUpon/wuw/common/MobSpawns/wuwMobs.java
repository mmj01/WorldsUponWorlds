package com.WorldsUpon.wuw.common.MobSpawns;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class wuwMobs {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "wuw");

    public static final Supplier<EntityType<wuwZombie>> WUW_ZOMBIE = ENTITY_TYPES.register("wuw_zombie",
            () -> EntityType.Builder.of(wuwZombie::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.95f)
                    .build("wuw_zombie")
    );

    public static final Supplier<EntityType<wuwSkeleton>> WUW_SKELETON = ENTITY_TYPES.register("wuw_skeleton",
            () -> EntityType.Builder.of(wuwSkeleton::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.99f)
                    .build("wuw_skeleton")
    );

    public static final Supplier<EntityType<wuwEnderman>> WUW_ENDERMAN = ENTITY_TYPES.register("wuw_enderman",
            () -> EntityType.Builder.of(wuwEnderman::new, MobCategory.CREATURE)
                    .sized(0.6f, 2.9f)
                    .build("wuw_enderman")
    );

    public static final Supplier<EntityType<wuwCreeper>> WUW_CREEPER = ENTITY_TYPES.register("wuw_creeper",
            () -> EntityType.Builder.of(wuwCreeper::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.7f)
                    .build("wuw_creeper")
    );

    public static final Supplier<EntityType<wuwSpider>> WUW_SPIDER = ENTITY_TYPES.register("wuw_spider",
            () -> EntityType.Builder.of(wuwSpider::new, MobCategory.CREATURE)
                    .sized(1.4f, 0.9f)
                    .build("wuw_spider")
    );
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}