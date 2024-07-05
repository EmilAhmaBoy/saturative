package dev.emilahmaboy.saturative.mixin.appleskin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.HungerManager;
import dev.emilahmaboy.saturative.api.HungerManagerValues;

import static dev.emilahmaboy.saturative.api.HungerManagerValues.foodBarDelta;
import static dev.emilahmaboy.saturative.api.HungerManagerValues.foodBarSize;


@Pseudo
@Mixin(targets = "squeek.appleskin.client.HUDOverlayHandler$OffsetsCache")
public class HUDOverlayHandlerMixinOffsetsCache {
    //? if >=1.20.6
    /*
    @ModifyConstant(
            method = "generate",
            constant = @Constant(
                    intValue = 10,
                    ordinal = 4
            )
    )
    private int modifyFoodCount(int ignored) {
        return foodBarSize;
    }

    @ModifyConstant(
            method = "generate",
            constant = @Constant(
                    intValue = 8,
                    ordinal = 1
            )
    )
    private int modifyFoodDelta(int ignored) {
        return foodBarDelta;
    }

    @Inject(
            method = "generate",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/entity/player/HungerManager;getFoodLevel()I",
                            ordinal = 0
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;ceil(D)D",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            )
    )
    private void modifyShouldAnimatedFood(int guiTicks, PlayerEntity player, CallbackInfo ci, @Local(ordinal = 1) LocalBooleanRef shouldAnimatedFood) {
        HungerManager hungerManager = player.getHungerManager();
        int foodLevel = hungerManager.getFoodLevel();
        float saturationLevel = hungerManager.getSaturationLevel();

        boolean isFoodBarShouldBeAnimated = HungerManagerValues.of(foodLevel, saturationLevel).isFoodBarShouldBeAnimated(guiTicks);
        if (isFoodBarShouldBeAnimated) {
            shouldAnimatedFood.set(true);
        }
    }
    */
}