package dev.emilahmaboy.saturative.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Shadow @Final private PlayerAbilities abilities;

    @Shadow protected HungerManager hungerManager;

    @ModifyReturnValue(
            method = "canConsume",
            at = @At("TAIL")
    )
    private boolean canConsume(boolean original, boolean ignoreHunger) {
        return original;
    }
}
