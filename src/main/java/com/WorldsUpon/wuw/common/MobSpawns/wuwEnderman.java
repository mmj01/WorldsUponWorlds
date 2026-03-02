package com.WorldsUpon.wuw.common.MobSpawns;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.level.Level;

public class wuwEnderman extends EnderMan {

    public wuwEnderman(EntityType<? extends EnderMan> type, Level level) {
        super(type, level);
    }

        public static AttributeSupplier.Builder createAttributes() {
        return EnderMan.createAttributes();
    }

}