package dev.emilahmaboy.saturative.mixin.appleskin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

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
        return 12;
    }

    @ModifyConstant(
            method = "generate",
            constant = @Constant(
                    intValue = 8,
                    ordinal = 1
            )
    )
    private int modifyFoodDelta(int ignored) {
        return 7;
    }
    */
}