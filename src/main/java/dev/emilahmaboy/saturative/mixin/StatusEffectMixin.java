package dev.emilahmaboy.saturative.mixin;

import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;


@Mixin(StatusEffect.class)
public class StatusEffectMixin {
    @ModifyConstant(
            method = "applyUpdateEffect",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/entity/damage/DamageSources;wither()Lnet/minecraft/entity/damage/DamageSource;"
                    )
            ),
            constant = @Constant(
                    floatValue = 0.005F,
                    ordinal = 0
            )
    )
    public float moreExhaustion(float constant) {
        return 0.03F;
    }
}
