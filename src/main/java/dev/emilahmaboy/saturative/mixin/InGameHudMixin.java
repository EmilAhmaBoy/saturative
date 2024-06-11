package dev.emilahmaboy.saturative.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emilahmaboy.saturative.integrations.farmersdelight.FarmersDelightModEffectsInstance;

import net.minecraft.client.gui.hud.InGameHud;


@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow private int ticks;

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow @Final private static Identifier ICONS;

    @Shadow @Final private Random random;

    @Unique
    private static Identifier MOD_ICONS_TEXTURE;
    static {
        if (FabricLoader.getInstance().isModLoaded("farmersdelight")) {
            MOD_ICONS_TEXTURE = new Identifier("farmersdelight", "textures/gui/fd_icons.png");
        }
    }

    @ModifyConstant(
            method = "renderStatusBars",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/gui/hud/InGameHud;getHeartCount(Lnet/minecraft/entity/LivingEntity;)I"
                    )
            ),
            constant = @Constant(
                    intValue = 10,
                    ordinal = 0
            )
    )
    private int noFoodBarRender(int ignored) {
        return 0;
    }

    @Inject(
            method = "renderStatusBars",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/gui/hud/InGameHud;getHeartCount(Lnet/minecraft/entity/LivingEntity;)I"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void renderFoodBar(DrawContext context, CallbackInfo ci) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        HungerManager hungerManager = playerEntity.getHungerManager();

        float foodLevel = (float) hungerManager.getFoodLevel();

        Identifier icons = ICONS;
        int uFull = 52;
        int uEmpty = 16;
        int vFull = 27;
        int vEmpty = 27;


        if (FarmersDelightModEffectsInstance.getInstance().hasNourishmentEffect(playerEntity)) {
            icons = MOD_ICONS_TEXTURE;
            uFull = 18 + ((foodLevel >= 280.0F || (foodLevel > 120.0F && (hungerManager.getSaturationLevel() <= 1.0F || hungerManager.getSaturationLevel() > 3.0F))) ? 18 : 0);
            uEmpty = 0;
            vFull = 0;
            vEmpty = 0;
        }

        int count = 12;  // Count of food icons
        int delta = 7;  // Distance between food icons
        int rightEdge = this.scaledWidth / 2 + 91 - 9;

        for (int i = 0; i < count; i++) {
            int _uFull = uFull;
            int _uEmpty = uEmpty;
            int _vFull = vFull;
            int _vEmpty = vEmpty;

            float f_i = (float) i;
            int y = this.scaledHeight - 39;
            if (foodLevel < 20 || foodLevel >= 300) {
                y += this.random.nextInt(3) - 1;
            } else if (playerEntity.getHungerManager().getSaturationLevel() <= 0.0F && this.ticks % foodLevel == 0) {
                y += this.random.nextInt(3) - 1;
            }
            int x = rightEdge - delta * i;
            // Hunger
            if (playerEntity.hasStatusEffect(StatusEffects.HUNGER)) {
                if (icons == ICONS) {
                    _uFull += 36;
                    _uEmpty = 16 + 13 * 9;
                }
            }
            context.drawTexture(icons, x, y, _uEmpty, _vEmpty, 9, 9);
            if (foodLevel < 100) {
                float foodSegment = 100.0F / (float) count;
                float nearestFoodSegment = foodSegment * ((float) (count - i - 1));
                if (nearestFoodSegment < foodLevel) {
                    float brilliance = foodLevel - nearestFoodSegment;
                    context.setShaderColor(0.4F, 0.7F, 0.6F, 0.75F);
                    context.drawTexture(icons, x, y, _uFull, _vFull, (int) Math.min(9.0F, (brilliance / foodSegment) * 9.0F), 9);
                }
                context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            } else if (foodLevel < 300) {
                context.setShaderColor(0.4F, 0.7F, 0.6F, 0.75F);
                context.drawTexture(icons, x, y, _uFull, _vFull, 9, 9);

                float foodSegment = 200.0F / (float) count;
                float nearestFoodSegment = 100.0f + foodSegment * ((float) (count - i - 1));
                if (nearestFoodSegment < foodLevel) {
                    float brilliance = foodLevel - nearestFoodSegment;
                    context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    context.drawTexture(icons, x, y, _uFull, _vFull, (int) Math.min(9.0F, (brilliance / foodSegment) * 9.0F), 9);
                }
                context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            } else if (foodLevel < 350) {
                context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                context.drawTexture(icons, x, y, _uFull, _vFull, 9, 9);

                float foodSegment = 50.0F / (float) count;
                float nearestFoodSegment = 300.0f + foodSegment * ((float) (count - i - 1));
                if (nearestFoodSegment < foodLevel) {
                    float brilliance = foodLevel - nearestFoodSegment;
                    context.setShaderColor(1.0F, 0.7F, 0.3F, 1.0F);
                    context.drawTexture(icons, x, y, _uFull, _vFull, (int) Math.min(9.0F, (brilliance / foodSegment) * 9.0F), 9);
                }
                context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                float multiplier = 1.0F / (float) Math.max(1.0F, Math.pow(foodLevel - 300.0F, 0.1F));
                context.setShaderColor(1.0F, 0.7F * multiplier, 0.3F * multiplier, 1.0F);
                context.drawTexture(icons, x, y, _uFull, _vFull, 9, 9);
                context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
