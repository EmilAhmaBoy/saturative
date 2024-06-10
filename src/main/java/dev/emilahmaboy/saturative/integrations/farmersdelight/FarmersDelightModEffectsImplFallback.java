package dev.emilahmaboy.saturative.integrations.farmersdelight;

import net.minecraft.entity.player.PlayerEntity;

public class FarmersDelightModEffectsImplFallback implements FarmersDelightModEffectsService {
    @Override
    public boolean hasNourishmentEffect(PlayerEntity playerEntity) {
        return false;
    }
}
