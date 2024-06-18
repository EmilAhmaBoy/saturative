package dev.emilahmaboy.saturative.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emilahmaboy.saturative.integrations.farmersdelight.FarmersDelightModEffectsInstance;

import net.minecraft.client.gui.hud.InGameHud;


@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow private int ticks;

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    //? if =1.20.1 {
    @Shadow @Final private static Identifier ICONS;
    //?} else
    /*
    @Shadow @Final private static Identifier FOOD_EMPTY_TEXTURE;
    @Shadow @Final private static Identifier FOOD_FULL_TEXTURE;
    @Shadow @Final private static Identifier FOOD_EMPTY_HUNGER_TEXTURE;
    @Shadow @Final private static Identifier FOOD_FULL_HUNGER_TEXTURE;
    */

    @Shadow @Final private Random random;

    @Shadow private long heartJumpEndTick;
    @Unique
    private static Identifier MOD_ICONS_TEXTURE;
    static {
        if (FabricLoader.getInstance().isModLoaded("farmersdelight")) {
            //? if <1.21 {
            MOD_ICONS_TEXTURE = new Identifier("farmersdelight", "textures/gui/fd_icons.png");
            //?} else
            /*
            MOD_ICONS_TEXTURE = Identifier.of("farmersdelight", "textures/gui/fd_icons.png");
            */
        }
    }

    //? if <1.20.6 {
    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

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
        renderFood(context, this.getCameraPlayer(), this.scaledHeight - 39, this.scaledWidth / 2 + 91);
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
    //?} else
    /*
    @Inject(method = "renderFood", at = @At("HEAD"))
    private void renderFoodBar(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        renderFood(context, this.getCameraPlayer(), top, right);
    }

    @ModifyConstant(
            method = "renderFood",
            constant = @Constant(
                    intValue = 10,
                    ordinal = 0
            )
    )
    private int noFoodBarRender(int ignored) {
        return 0;
    }
    */

    @Unique
    private void renderFood(DrawContext context, PlayerEntity player, int top, int right) {
        HungerManager hungerManager = player.getHungerManager();

        float foodLevel = (float) hungerManager.getFoodLevel();

        Identifier iconsFull;
        Identifier iconsEmpty;
        int uFull;
        int uEmpty;
        int vFull;
        int vEmpty;
        boolean isUV;

        //? if <1.20.4 {
        iconsFull = ICONS;
        iconsEmpty = ICONS;
        uFull = 52;
        uEmpty = 16;
        vFull = 27;
        vEmpty = 27;
        isUV = true;
        //?} else {
        /*
        iconsFull = FOOD_FULL_TEXTURE;
        iconsEmpty = FOOD_EMPTY_TEXTURE;
        uFull = 0;
        uEmpty = 0;
        vFull = 0;
        vEmpty = 0;
        isUV = false;
        */
        //?}


        if (FarmersDelightModEffectsInstance.getInstance().hasNourishmentEffect(player)) {
            iconsFull = MOD_ICONS_TEXTURE;
            iconsEmpty = MOD_ICONS_TEXTURE;
            uFull = 18 + ((foodLevel >= 280.0F || (foodLevel > 120.0F && (hungerManager.getSaturationLevel() <= 1.0F || hungerManager.getSaturationLevel() > 3.0F))) ? 18 : 0);
            uEmpty = 0;
            vFull = 0;
            vEmpty = 0;
            isUV = true;
        }

        int count = 12;  // Count of food icons
        int delta = 7;  // Distance between food icons
        int rightEdge = right - 9;

        for (int i = 0; i < count; i++) {
            int _uFull = uFull;
            int _uEmpty = uEmpty;
            int _vFull = vFull;
            int _vEmpty = vEmpty;

            int y = top;
            if (foodLevel < 20 || foodLevel >= 300) {
                y += this.random.nextInt(3) - 1;
            } else if (hungerManager.getSaturationLevel() <= 0.0F && this.ticks % foodLevel == 0) {
                y += this.random.nextInt(3) - 1;
            }
            int x = rightEdge - delta * i;
            // Hunger
            if (player.hasStatusEffect(StatusEffects.HUNGER)) {
                //? if <1.20.4 {
                if (iconsFull == ICONS) {
                    _uFull += 36;
                    _uEmpty = 16 + 13 * 9;
                }
                //?} else {
                /*
                if (iconsFull == FOOD_FULL_TEXTURE) {
                    iconsFull = FOOD_FULL_HUNGER_TEXTURE;
                    iconsEmpty = FOOD_EMPTY_HUNGER_TEXTURE;
                    uFull = 0;
                    uEmpty = 0;
                    vFull = 0;
                    vEmpty = 0;
                    isUV = false;
                }
                */
                //?}
            }
            this.drawFoodIcon(context, iconsEmpty, x, y, _uEmpty, _vEmpty, 9, 9, 1.0F, 1.0F, 1.0F, 1.0F, isUV);

            if (foodLevel < 100) {
                float foodSegment = 100.0F / (float) count;
                float nearestFoodSegment = foodSegment * ((float) (count - i - 1));
                if (nearestFoodSegment < foodLevel) {
                    float brilliance = foodLevel - nearestFoodSegment;
                    this.drawFoodIcon(context, iconsFull, x, y, _uFull, _vFull, (int) Math.min(9.0F, (brilliance / foodSegment) * 9.0F), 9, 0.45F, 0.83F, 0.69F, 0.75F, isUV);
                }
            } else if (foodLevel < 300) {
                this.drawFoodIcon(context, iconsFull, x, y, _uFull, _vFull, 9, 9, 0.4F, 0.7F, 0.6F, 0.75F, isUV);

                float foodSegment = 200.0F / (float) count;
                float nearestFoodSegment = 100.0f + foodSegment * ((float) (count - i - 1));
                if (nearestFoodSegment < foodLevel) {
                    float brilliance = foodLevel - nearestFoodSegment;
                    this.drawFoodIcon(context, iconsFull, x, y, _uFull, _vFull, (int) Math.min(9.0F, (brilliance / foodSegment) * 9.0F), 9, 1.0F, 1.0F, 1.0F, 1.0F, isUV);
                }
            } else if (foodLevel < 350) {
                this.drawFoodIcon(context, iconsFull, x, y, _uFull, _vFull, 9, 9, 1.0F, 1.0F, 1.0F, 1.0F, isUV);

                float foodSegment = 50.0F / (float) count;
                float nearestFoodSegment = 300.0f + foodSegment * ((float) (count - i - 1));
                if (nearestFoodSegment < foodLevel) {
                    float brilliance = foodLevel - nearestFoodSegment;
                    this.drawFoodIcon(context, iconsFull, x, y, _uFull, _vFull, (int) Math.min(9.0F, (brilliance / foodSegment) * 9.0F), 9, 1.0F, 0.7F, 0.3F, 1.0F, isUV);
                }
            } else {
                float multiplier = 1.0F / (float) Math.max(1.0F, Math.pow(foodLevel - 300.0F, 0.1F));
                this.drawFoodIcon(context, iconsFull, x, y, _uFull, _vFull, 9, 9, 1.0F, 0.7F * multiplier, 0.3F * multiplier, 1.0F, isUV);
            }
        }
    }

    @Unique
    private void drawFoodIcon(DrawContext context, Identifier icon, int x, int y, int u, int v, int width, int height, float r, float g, float b, float a, boolean isUV) {
        context.setShaderColor(r, g, b, a);
        //? if <1.20.4 {
        context.drawTexture(icon, x, y, u, v, width, height);
        //?} else
        /*
        if (isUV) {
            context.drawTexture(icon, x, y, u, v, width, height);
        } else {
            context.enableScissor(x, y, x + width, y + height);
            context.drawGuiTexture(icon, x, y, Math.max(width, height), Math.max(width, height));
            context.disableScissor();
        }
        */

        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
