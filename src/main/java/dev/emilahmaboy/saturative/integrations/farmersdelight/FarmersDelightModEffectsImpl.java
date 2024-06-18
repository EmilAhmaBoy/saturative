package dev.emilahmaboy.saturative.integrations.farmersdelight;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import vectorwing.farmersdelight.common.registry.ModEffects;


public class FarmersDelightModEffectsImpl implements FarmersDelightModEffectsService {
    @Override
    public boolean hasNourishmentEffect(PlayerEntity playerEntity) {
        //? if <1.20.6 {
        return playerEntity.hasStatusEffect(ModEffects.NOURISHMENT.get());
        //?} else
        /*
        RegistryEntry<StatusEffect> registryEntry = Registries.STATUS_EFFECT.getEntry(ModEffects.NOURISHMENT.get());
        return playerEntity.hasStatusEffect(registryEntry);
        */
    }
}
