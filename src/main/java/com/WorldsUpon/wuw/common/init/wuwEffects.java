package com.WorldsUpon.wuw.common.init;

import com.WorldsUpon.wuw.WorldsUponWorlds;
import com.WorldsUpon.wuw.common.effect.XrayEye;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class wuwEffects {

    // 1. Create the Registry Wrapper
    // This tells the game: "I have a list of Mob Effects for the mod 'wuw'"
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, WorldsUponWorlds.MODID);

    // 2. Register the Specific Effect
    // "void_sickness" will be the registry name (e.g., wuw:void_sickness)
    // VoidSicknessEffect::new tells it to create a new instance of your class
    public static final DeferredHolder<MobEffect, XrayEye> XRAYEYE =
            MOB_EFFECTS.register("xray_eye", XrayEye::new);

}