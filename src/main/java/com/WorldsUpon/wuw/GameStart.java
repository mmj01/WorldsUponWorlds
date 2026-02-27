package com.WorldsUpon.wuw;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Set;

public class GameStart {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerLevel targetDim = player.getServer().getLevel(wuwDimensions.TOP_SECTOR_A);

            if (targetDim != null) {
                double x = 0.0, y = 1025.0, z = 0.0;
                float yaw = 0f, pitch = 0f;

                player.teleportTo(targetDim, x, y, z, Set.of(), yaw, pitch);
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20000, 1));
                player.displayClientMessage(Component.literal("Dimension Loaded"), true);
            }
        }
    }
}
