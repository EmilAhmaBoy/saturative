package dev.emilahmaboy.saturative.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.emilahmaboy.saturative.api.HungerManagerValues;
import dev.emilahmaboy.saturative.api.NewHungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.entity.player.HungerManager;

import static dev.emilahmaboy.saturative.api.HungerManagerValues.*;


@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Shadow private int foodLevel = defaultFoodLevel;
    // Too low food: <20
    // Low food: <100
    // Normal: <300
    // Too saturated: >=300

    @Shadow private int prevFoodLevel;

    @Shadow private float saturationLevel = defaultSaturationLevel;

    @Shadow private float exhaustion = defaultExhaustionLevel;

    @Shadow private int foodTickTimer;

    @Shadow public abstract void addExhaustion(float exhaustion);

    @ModifyReturnValue(
            method = "isNotFull",
            at = @At("TAIL")
    )
    public boolean isNotFull(boolean original) {
        return this.foodLevel < maxFoodLevel;
    }

    /**
     * @author EmilAhmaBoy
     * @reason Saturative mod overwrites HungerManager
     */
    //? if <1.20.6 {
    @Overwrite
    public void add(int food, float saturationModifier) {
        this.foodLevel = HungerManagerValues.of(this.foodLevel, this.saturationLevel).addFoodLevel(food * nutritionModifier).getFoodLevel();
        this.saturationLevel = HungerManagerValues.of(this.foodLevel, this.saturationLevel).addSaturationLevel(saturationModifier * food * 2.0F).getSaturationLevel();
    }
    //?} else
    /*
    @Overwrite
    private void addInternal(int nutrition, float saturation) {
        this.foodLevel = HungerManagerValues.of(this.foodLevel, this.saturationLevel).addFoodLevel(nutrition * nutritionModifier).getFoodLevel();
        this.saturationLevel = HungerManagerValues.of(this.foodLevel, this.saturationLevel).addSaturationLevel(saturation).getSaturationLevel();
    }
    */

    /**
     * @author EmilAhmaBoy
     * @reason Saturative mod overwrites HungerManager
     */
    @Overwrite
    public void update(PlayerEntity player) {
        HungerManagerValues hungerManagerValues = HungerManagerValues.of(this.foodLevel, this.saturationLevel, this.exhaustion, this.foodTickTimer);
        NewHungerManager.updatePlayerHunger(player, hungerManagerValues);

        this.prevFoodLevel = this.foodLevel;
        this.foodLevel = hungerManagerValues.getFoodLevel();
        this.saturationLevel = hungerManagerValues.getSaturationLevel();
        this.exhaustion = hungerManagerValues.getExhaustionLevel();
        this.foodTickTimer = hungerManagerValues.getFoodTickTimer();
    }
}
