package dev.emilahmaboy.saturative.integrations.farmersdelight;


import net.fabricmc.loader.api.FabricLoader;

import java.util.Iterator;
import java.util.ServiceLoader;

public class FarmersDelightModEffectsInstance {;
    public static FarmersDelightModEffectsService getInstance() {
        if (!FabricLoader.getInstance().isModLoaded("farmersdelight")) {
            return new FarmersDelightModEffectsImplFallback();
        } else {
            ServiceLoader<FarmersDelightModEffectsService> loader = ServiceLoader.load(FarmersDelightModEffectsService.class);
            return loader.findFirst().orElseThrow();
        }
    }
}
