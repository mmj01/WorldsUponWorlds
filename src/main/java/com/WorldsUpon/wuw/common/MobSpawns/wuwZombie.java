package com.WorldsUpon.wuw.common.MobSpawns;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class wuwZombie extends Zombie {

    public wuwZombie(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()

                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE,1.0D);
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!super.hurt(source, amount)) {
            return false;
        } else if (!(this.level() instanceof ServerLevel)) {
            return false;
        } else {
            ServerLevel serverlevel = (ServerLevel)this.level();
            LivingEntity livingentity = this.getTarget();
            if (livingentity == null && source.getEntity() instanceof LivingEntity) {
                livingentity = (LivingEntity)source.getEntity();
            }

            if (livingentity != null && (double)this.random.nextFloat() < this.getAttributeValue(Attributes.SPAWN_REINFORCEMENTS_CHANCE) && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                int i = Mth.floor(this.getX());
                int j = Mth.floor(this.getY());
                int k = Mth.floor(this.getZ());

                Zombie zombie = (Zombie) this.getType().create(this.level());

                if (zombie == null) return true;

                for(int l = 0; l < 50; ++l) {
                    int i1 = i + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                    int j1 = j + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                    int k1 = k + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                    BlockPos blockpos = new BlockPos(i1, j1, k1);
                    EntityType<?> entitytype = zombie.getType();
                    if (SpawnPlacements.isSpawnPositionOk(entitytype, this.level(), blockpos) && SpawnPlacements.checkSpawnRules(entitytype, serverlevel, MobSpawnType.REINFORCEMENT, blockpos, this.level().random)) {
                        zombie.setPos((double)i1, (double)j1, (double)k1);
                        if (!this.level().hasNearbyAlivePlayer((double)i1, (double)j1, (double)k1, (double)7.0F) && this.level().isUnobstructed(zombie) && this.level().noCollision(zombie) && !this.level().containsAnyLiquid(zombie.getBoundingBox())) {
                            zombie.setTarget(livingentity);
                            zombie.finalizeSpawn(serverlevel, this.level().getCurrentDifficultyAt(zombie.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null);
                            serverlevel.addFreshEntityWithPassengers(zombie);
                            AttributeInstance attributeinstance = this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
                            AttributeModifier attributemodifier = attributeinstance.getModifier(net.minecraft.resources.ResourceLocation.withDefaultNamespace("reinforcement_caller_charge"));
                            double d0 = attributemodifier != null ? attributemodifier.amount() : 0.0D;
                            attributeinstance.removeModifier(net.minecraft.resources.ResourceLocation.withDefaultNamespace("reinforcement_caller_charge"));
                            attributeinstance.addPermanentModifier(new AttributeModifier(net.minecraft.resources.ResourceLocation.withDefaultNamespace("reinforcement_caller_charge"), d0 - 0.05D, AttributeModifier.Operation.ADD_VALUE));
                            zombie.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(new AttributeModifier(net.minecraft.resources.ResourceLocation.withDefaultNamespace("reinforcement_callee_charge"), -0.05D, AttributeModifier.Operation.ADD_VALUE));
                            break;
                        }
                    }
                }
            }
            return true;
        }
    }


}