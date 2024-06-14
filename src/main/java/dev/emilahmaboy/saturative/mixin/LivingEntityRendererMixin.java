package dev.emilahmaboy.saturative.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;


@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @ModifyConstant(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getOverlay(Lnet/minecraft/entity/LivingEntity;F)I"
                    )
            ),
            constant = @Constant(
                    floatValue = 1.0F,
                    ordinal = 0
            )
    )
    private float hungerManagerRenderColorModifierR(float constant, T livingEntity) {
        if (livingEntity instanceof PlayerEntity player) {
            if (player.isCreative()) {
                HungerManager hungerManager = player.getHungerManager();
                if (hungerManager.getFoodLevel() > 300) {
                    return Math.max(0.0F, constant - ((float) hungerManager.getFoodLevel() - 300.0F) / 190.0F);
                } else if (hungerManager.getFoodLevel() < 100) {
                    return Math.max(0.0F, constant - (100.0F - (float) hungerManager.getFoodLevel()) / 210.0F);
                }
            }
        }
        return constant;
    }

    @ModifyConstant(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getOverlay(Lnet/minecraft/entity/LivingEntity;F)I"
                    )
            ),
            constant = @Constant(
                    floatValue = 1.0F,
                    ordinal = 1
            )
    )
    private float hungerManagerRenderColorModifierG(float constant, T livingEntity) {
        return constant;
    }

    @ModifyConstant(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getOverlay(Lnet/minecraft/entity/LivingEntity;F)I"
                    )
            ),
            constant = @Constant(
                    floatValue = 1.0F,
                    ordinal = 2
            )
    )
    private float hungerManagerRenderColorModifierB(float constant, T livingEntity) {
        if (livingEntity instanceof PlayerEntity player) {
            if (player.isCreative()) {
                HungerManager hungerManager = player.getHungerManager();
                if (hungerManager.getFoodLevel() > 300) {
                    return Math.max(0.0F, constant - ((float) hungerManager.getFoodLevel() - 300.0F) / 200.0F);
                } else if (hungerManager.getFoodLevel() < 100) {
                    return Math.max(0.0F, constant - (100.0F - (float) hungerManager.getFoodLevel()) / 200.0F);
                }
            }
        }
        return constant;
    }
}
