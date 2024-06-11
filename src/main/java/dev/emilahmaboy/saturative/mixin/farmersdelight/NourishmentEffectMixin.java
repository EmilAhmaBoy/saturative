package dev.emilahmaboy.saturative.mixin.farmersdelight;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.*;


@Pseudo
@Mixin(targets = "vectorwing.farmersdelight.common.effect.NourishmentEffect")
public class NourishmentEffectMixin extends StatusEffect {
    protected NourishmentEffectMixin(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @ModifyExpressionValue(
            method = "applyUpdateEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/HungerManager;getFoodLevel()I"
            )
    )
    private int addFoodRequirements(int ignored) {
        return 0;
//        HungerManager foodData = player.getHungerManager();
//        if (foodData.getFoodLevel() < 280) {
//            if (foodData.getFoodLevel() > 120 && (foodData.getSaturationLevel() <= 1.0F || foodData.getSaturationLevel() > 3.0F)) {
//                // not saturating
//                return 20;
//            }
//            // saturating
//            return 0;
//        } else {
//            // not saturating
//            return 20;
//        }
    }

    @ModifyExpressionValue(
            method = "applyUpdateEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;canFoodHeal()Z"
            )
    )
    private boolean removeCondition(boolean original) {
        return true;
    }
}
