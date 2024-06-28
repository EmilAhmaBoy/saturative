package dev.emilahmaboy.saturative.mixin.appleskin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import squeek.appleskin.ModConfig;
import squeek.appleskin.helpers.FoodHelper;
//? if <1.20.6 {
import net.minecraft.item.FoodComponent;
//?} else
/*
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.DataComponentTypes;
*/


@Pseudo
@Mixin(targets = "squeek.appleskin.client.HUDOverlayHandler")
public abstract class HUDOverlayHandlerMixin {
    //? if <1.20.6 {
    @ModifyConstant(
            method = "generateBarOffsets",
            constant = @Constant(
                    intValue = 10,
                    ordinal = 4
            )
    )
    private int modifyFoodCount(int ignored) {
        return 12;
    }
    @ModifyConstant(
            method = "generateBarOffsets",
            constant = @Constant(
                    intValue = 8,
                    ordinal = 1
            )
    )
    private int modifyFoodDelta(int ignored) {
        return 7;
    }
    @Inject(
            method = "generateBarOffsets",
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
    private void modifyShouldAnimatedFood(int top, int left, int right, int ticks, PlayerEntity player, CallbackInfo ci, @Local(ordinal = 1) LocalBooleanRef shouldAnimatedFood) {
        HungerManager hungerManager = player.getHungerManager();
        int foodLevel = hungerManager.getFoodLevel();
        if (foodLevel < 20 || foodLevel >= 300) {
            shouldAnimatedFood.set(true);
        } else if (hungerManager.getSaturationLevel() <= 0.0F && ticks % foodLevel == 0) {
            shouldAnimatedFood.set(true);
        }
    }
    //?}
    @ModifyExpressionValue(
            method = "drawSaturationOverlay(Lnet/minecraft/client/gui/DrawContext;FFLnet/minecraft/client/MinecraftClient;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;min(FF)F",
                    ordinal = 0
            )
    )
    private float drawSaturationOverlay(float original, DrawContext context, float saturationGained, float saturationLevel, MinecraftClient mc, int right, int top, float alpha) {
        PlayerEntity player = mc.player;
        if (player != null) {
            int foodLevel = player.getHungerManager().getFoodLevel();
            ItemStack heldItem = player.getMainHandStack();
            if (ModConfig.INSTANCE.showFoodValuesHudOverlayWhenOffhand &&
                    //? if <1.20.6 {
                    !FoodHelper.canConsume(heldItem, player)
                    //?} else
                    /* heldItem.get(DataComponentTypes.FOOD) != null ? !FoodHelper.canConsume(player, (FoodComponent) heldItem.get(DataComponentTypes.FOOD)) : true */
            ) {
                heldItem = player.getOffHandStack();
            }

            //? if <1.20.6 {
            FoodComponent foodComponent = heldItem.getItem().getFoodComponent();
            int modifiedFoodValue = foodComponent != null ? foodComponent.getHunger() : 0;
            //?} else
            /*
            FoodComponent foodComponent = (FoodComponent) heldItem.get(DataComponentTypes.FOOD);
            int modifiedFoodValue = foodComponent != null ? foodComponent.nutrition() : 0;
            */

            int modifiedFood = modifiedFoodValue * 5;
            int newFoodLevel = Math.min(foodLevel + modifiedFood, 400);

            return Math.min(original, (float) newFoodLevel / 18.0F);
        }
        return original;
    }


    @ModifyConstant(
            method = "drawExhaustionOverlay*",
            constant = @Constant(
                    floatValue = 81.0F,
                    ordinal = 0
            )
    )
    private float dontDrawExhaustionOverlay(float ignored) {
        return 0.0F;
    }

    @ModifyExpressionValue(
            method = "drawHungerOverlay(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/client/MinecraftClient;IIFZ)V",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;ceil(D)D", ordinal = 0)
    )
    private double dontDrawHungerOverlay(double original) {
        return 0;
    }
}
