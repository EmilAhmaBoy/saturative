package dev.emilahmaboy.saturative.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;

import net.minecraft.item.PotionItem;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(
            method = "finishUsing",
            at = @At(
                    value = "HEAD"
            )
    )
    private void decreaseRedundantFood(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (user instanceof PlayerEntity player) {
            float exhaustion = 6.0F;
            int duration = 100;
            int amplifier = 1;
            List<StatusEffectInstance> list = PotionUtil.getPotionEffects(stack);
            for (StatusEffectInstance statusEffectInstance : list) {
                if (statusEffectInstance.getEffectType() == StatusEffects.SLOWNESS) {
                    duration *= 3;
                    amplifier = 0;
                    exhaustion = 1.0F;
                } else if (statusEffectInstance.getEffectType() == StatusEffects.SPEED) {
                    duration = (int) (duration / 3);
                    amplifier = 4;
                    exhaustion = 7.0F;
                } else {
                    duration = (int) (duration / 1.5);
                }
            }

            HungerManager hungerManager = player.getHungerManager();
            int foodLevel = hungerManager.getFoodLevel();
            if (foodLevel >= 300) {
                hungerManager.addExhaustion(exhaustion);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, duration, amplifier, true, false, false));
            }
        }
    }
}
