package com.WorldsUpon.wuw.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class XrayEye extends MobEffect {

    public XrayEye() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier){

        return super.applyEffectTick(entity,amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
