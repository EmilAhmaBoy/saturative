package dev.emilahmaboy.saturative.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.IceBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.player.HungerManager;


@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Unique private int foodDigestionTimer = 0;

    @Shadow private int foodLevel = 150;
    // Too low food: <20
    // Low food: <100
    // Normal: <300
    // Too saturated: >=300

    @Shadow private int prevFoodLevel;

    @Shadow private float saturationLevel;

    @Shadow private float exhaustion;

    @Shadow private int foodTickTimer;

    @Shadow public abstract void addExhaustion(float exhaustion);

    @ModifyReturnValue(
            method = "isNotFull",
            at = @At("TAIL")
    )
    public boolean isNotFull(boolean original) {
        return foodDigestionTimer <= 0;
    }

    /**
     * @author EmilAhmaBoy
     * @reason Saturative mod overwrites HungerManager
     */
    @Overwrite
    public void add(int food, float saturationModifier) {
        this.foodLevel = Math.min(this.foodLevel + food * 4 + (int) (saturationModifier * (float) food) * 2, 400);
        this.saturationLevel = Math.min(this.saturationLevel + saturationModifier * food * 2.0F, (float) this.foodLevel / 20.0F);
    }

    @Inject(
            method = "eat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    public void eatDigestionTimer(Item item, ItemStack stack, CallbackInfo ci) {
        FoodComponent foodComponent = item.getFoodComponent();
        if (foodComponent != null) {
            int food = foodComponent.getHunger();
            foodDigestionTimer += 10 + 5 * food;
        }
    }

    @Unique
    public int getFoodDigestionTimer() {
        return foodDigestionTimer;
    }

    /**
     * @author EmilAhmaBoy
     * @reason Saturative mod overwrites HungerManager
     */
    @Overwrite
    public void update(PlayerEntity player) {
        foodDigestionTimer = foodDigestionTimer - 1;
        Difficulty difficulty = player.getWorld().getDifficulty();
        this.prevFoodLevel = this.foodLevel;
        boolean naturalRegenerative = player.getWorld().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);

        if (!player.isCreative()) {
            if (player.getHealth() < 16.0F) {
                this.addExhaustion(0.002F + (this.foodLevel > 300 ? 0.002F : 0.0F));
            }

            if (this.exhaustion > 1.5F) {
                this.exhaustion -= 1.5F;
                if (this.saturationLevel > 0.0F) {
                    this.saturationLevel = Math.max(this.saturationLevel - 0.4F, 0.0F);
                    if (this.saturationLevel > 0.0F && this.foodLevel > 200) {
                        this.saturationLevel = Math.max(this.saturationLevel - 0.4F, 0.0F);
                    }
                }
                if (this.saturationLevel <= 0.0F) {
                    this.foodLevel = Math.max(this.foodLevel - 2, 0);
                } else if (this.saturationLevel < 3.0F) {
                    this.foodLevel = Math.max(this.foodLevel - 1, 0);
                }
            }

            if (this.foodLevel >= 350) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1, false, false));
                ++this.foodTickTimer;
                if (this.foodTickTimer >= 80) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0, false, false));
                    this.addExhaustion(3.9F);
                    if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                        if (difficulty != Difficulty.PEACEFUL) {
                            player.damage(player.getDamageSources().starve(), 1.0F);
                        }
                    }
                    this.foodTickTimer = 0;
                }
            } else if (this.foodLevel >= 310) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0, false, false));
                ++this.foodTickTimer;
                if (this.foodTickTimer >= 100) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 160, 0, false, false));
                    this.addExhaustion(2.5F);
                    this.foodTickTimer = 0;
                }
            } else if (naturalRegenerative && this.saturationLevel > 0.0F && player.canFoodHeal() && this.foodLevel >= 120 && this.foodLevel < 310) {
                ++this.foodTickTimer;
                if (this.foodTickTimer >= 10) {
                    float f = Math.min(this.saturationLevel, 5.0F);
                    player.heal(f / 5.0F);
                    this.addExhaustion(f / 2.0F);
                    this.foodTickTimer = 0;
                }
            } else if (naturalRegenerative && this.saturationLevel <= 0.0F && player.canFoodHeal() && this.foodLevel >= 100 && this.foodLevel < 400 && player.getHealth() <= 16.0F) {
                ++this.foodTickTimer;
                if (this.foodTickTimer >= 3) {
                    this.foodLevel -= 10;
                    this.saturationLevel += 3.5F;
                    this.addExhaustion(0.2F);
                    this.foodTickTimer = 0;
                }
            } else if (naturalRegenerative && player.canFoodHeal() && this.foodLevel >= 80 && this.foodLevel < 120) {
                ++this.foodTickTimer;
                if (this.foodTickTimer >= 55) {
                    player.heal(1);
                    this.addExhaustion(2.3F);
                    this.foodTickTimer = 0;
                }
            } else if (this.foodLevel <= 80 && this.foodLevel > 40) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0, false, false));
            } else if (this.foodLevel <= 40) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1, false, false));
                ++this.foodTickTimer;
                if (this.foodTickTimer >= 40 + this.foodLevel * 2) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 120, 0, false, false));
                    if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                        if (difficulty != Difficulty.PEACEFUL) {
                            player.damage(player.getDamageSources().starve(), 1.0F);
                        }
                    }
                    this.foodTickTimer = 0;
                }
            }
        }
    }
}
