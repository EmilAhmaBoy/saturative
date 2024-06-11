package dev.emilahmaboy.saturative.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.minecraft.client.network.ClientPlayerEntity;


@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {
    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @ModifyConstant(
            method = "canSprint",
            constant = @Constant(
                    floatValue = 6.0F,
                    ordinal = 0
            )
    )
    public float changeMinimalFoodLevel(float constant) {
        return 999999.0F;
    }

    @ModifyReturnValue(
            method = "canSprint", at = @At(value = "TAIL")
    )
    public boolean checkFood(boolean original) {
        return original || (this.getHungerManager().getFoodLevel() > 60 && this.getHungerManager().getFoodLevel() < 320);
    }
}
