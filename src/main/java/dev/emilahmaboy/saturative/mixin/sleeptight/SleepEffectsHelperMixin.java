package dev.emilahmaboy.saturative.mixin.sleeptight;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Pseudo
@Mixin(targets = "net.mehvahdjukaar.sleep_tight.core.SleepEffectsHelper")
public class SleepEffectsHelperMixin {
    @ModifyConstant(
            method = "applySleepPenalties",
            constant = @Constant(
                    doubleValue = 20.0,
                    ordinal = 0
            )
    )
    private static double editClampValue(double constant) {
        return 300.0;
    }
}
