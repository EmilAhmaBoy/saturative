package dev.emilahmaboy.saturative.mixin.appleskin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;


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
    //?}

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
