package dev.emilahmaboy.saturative.mixin.appleskin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;


@Pseudo
@Mixin(targets = "squeek.appleskin.client.HUDOverlayHandler")
public abstract class HUDOverlayHandlerMixin {
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

    /**
     * @author EmilAhmaBoy
     * @reason It doesn't work very well with Saturative mod, so it temporarily removed
     */
    @Overwrite
    public void drawHungerOverlay(DrawContext context, int hungerRestored, int foodLevel, MinecraftClient mc, int right, int top, float alpha, boolean useRottenTextures) {

    }
}
