package com.WorldsUpon.wuw.common.event;

import com.WorldsUpon.wuw.WorldsUponWorlds;
import com.WorldsUpon.wuw.common.init.wuwEffects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = WorldsUponWorlds.MODID, bus = EventBusSubscriber.Bus.GAME)
public class Events {

    public static final ResourceKey<Level> FILLER_SECTOR =
            ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION,
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(WorldsUponWorlds.MODID, "filler_sector"));

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (event.getEntity() instanceof ServerPlayer player) {

            if (player.level().dimension() == FILLER_SECTOR) {


                boolean hasEffect = player.hasEffect(wuwEffects.XRAYEYE);
                boolean isRunningOut = hasEffect && player.getEffect(wuwEffects.XRAYEYE).getDuration() < 100;

                if (!hasEffect || isRunningOut) {
                    player.addEffect(new MobEffectInstance(
                            wuwEffects.XRAYEYE,
                            300,
                            0,
                            false,
                            true,
                            true
                    ));
                }
            }
        }
    }
}