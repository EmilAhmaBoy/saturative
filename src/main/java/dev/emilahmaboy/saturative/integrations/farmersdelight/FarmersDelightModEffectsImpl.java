package dev.emilahmaboy.saturative.integrations.farmersdelight;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.ServiceLoader;
import java.util.function.Supplier;


public class FarmersDelightModEffectsImpl implements FarmersDelightModEffectsService {
    @Override
    public boolean hasNourishmentEffect(PlayerEntity playerEntity) {
        return playerEntity.hasStatusEffect(ModEffects.NOURISHMENT.get());
    }
}
