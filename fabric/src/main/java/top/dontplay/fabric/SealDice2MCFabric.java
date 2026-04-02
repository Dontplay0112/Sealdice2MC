package top.dontplay.fabric;

import net.fabricmc.api.ModInitializer;

import top.dontplay.SealDice2MC;

public final class SealDice2MCFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SealDice2MC.init();
    }
}
