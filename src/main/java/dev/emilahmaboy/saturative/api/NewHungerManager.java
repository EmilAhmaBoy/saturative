package dev.emilahmaboy.saturative.api;

import dev.emilahmaboy.saturative.common.registries.DamageTypeRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

public class NewHungerManager {
    public static HungerManagerValues updatePlayerHunger(PlayerEntity player, HungerManagerValues values) {
        Difficulty difficulty = player.getWorld().getDifficulty();

        boolean naturalRegenerative = player.getWorld().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);


        if (!player.isCreative()) {
            if (player.getHealth() < player.getMaxHealth()) {
                values.addExhaustionLevel(0.002F + (values.getFoodLevel() > 300 ? 0.002F : 0.0F));
            }

            if (values.getExhaustionLevel() > 1.5F) {
                values.addExhaustionLevel(-1.5F);
                if (values.getSaturationLevel() > 0.0F) {
                    values.setSaturationLevel(Math.max(values.getSaturationLevel() - 0.4F, 0.0F));
                    if (values.getSaturationLevel() > 0.0F && values.getFoodLevel() > 200) {
                        values.setSaturationLevel(Math.max(values.getSaturationLevel() - 0.4F, 0.0F));
                    }
                }
                if (values.getSaturationLevel() <= 0.0F) {
                    values.setFoodLevel(Math.max(values.getFoodLevel() - 2, 0));
                } else if (values.getSaturationLevel() < 3.0F) {
                    values.setFoodLevel(Math.max(values.getFoodLevel() - 1, 0));
                }
            }

            if (values.getFoodLevel() >= 350) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1, false, false));

                values.tickFoodTickTimer();

                if (values.foodTickTimerReached(80)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0, false, false));
                    values.addExhaustionLevel(3.9F);
                    if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                        if (difficulty != Difficulty.PEACEFUL) {
                            player.damage(DamageTypeRegistry.getSource(player.getWorld(), DamageTypeRegistry.OVEREATING_DAMAGE_TYPE), 1.0F);
                        }
                    }

                    values.resetFoodTickTimer();
                }
            } else if (values.getFoodLevel() >= 310) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0, false, false));

                values.tickFoodTickTimer();

                if (values.foodTickTimerReached(100)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 160, 0, false, false));
                    values.addExhaustionLevel(2.5F);

                    values.resetFoodTickTimer();
                }
            } else if (naturalRegenerative && values.getSaturationLevel() > 0.0F && player.canFoodHeal() && values.getFoodLevel() >= 120 && values.getFoodLevel() < 310) {
                values.tickFoodTickTimer();

                if (values.foodTickTimerReached(10)) {
                    float f = Math.min(values.getSaturationLevel(), 5.0F);
                    player.heal(f / 5.0F);
                    values.addExhaustionLevel(f / 2.0F);

                    values.resetFoodTickTimer();
                }
            } else if (naturalRegenerative && values.getSaturationLevel() <= 0.0F && player.canFoodHeal() && values.getFoodLevel() >= 100 && values.getFoodLevel() < 400 && player.getHealth() <= player.getMaxHealth() / 1.15) {
                values.tickFoodTickTimer();

                if (values.foodTickTimerReached(3)) {
                    values.addFoodLevel(-10);
                    values.addSaturationLevel(3.5F);
                    values.addExhaustionLevel(0.2F);

                    values.resetFoodTickTimer();
                }
            } else if (naturalRegenerative && player.canFoodHeal() && values.getFoodLevel() >= 80 && values.getFoodLevel() < 120) {
                values.tickFoodTickTimer();

                if (values.foodTickTimerReached(55)) {
                    player.heal(1);
                    values.addExhaustionLevel(2.3F);

                    values.resetFoodTickTimer();
                }
            } else if (values.getFoodLevel() <= 80 && values.getFoodLevel() > 40) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0, false, false));
            } else if (values.getFoodLevel() <= 40) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1, false, false));

                values.tickFoodTickTimer();

                if (values.foodTickTimerReached(40 + values.getFoodLevel() * 2)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 120, 0, false, false));
                    if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                        if (difficulty != Difficulty.PEACEFUL) {
                            player.damage(player.getDamageSources().starve(), 1.0F);
                        }
                    }

                    values.resetFoodTickTimer();
                }
            }
        }

        return values;
    }
}
