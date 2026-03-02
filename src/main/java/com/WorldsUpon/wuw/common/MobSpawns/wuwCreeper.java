package com.WorldsUpon.wuw.common.MobSpawns;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;

public class wuwCreeper extends Creeper {

    public wuwCreeper(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
    }

        public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes();
    }

}