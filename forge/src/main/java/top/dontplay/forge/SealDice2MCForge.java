package top.dontplay.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import top.dontplay.SealDice2MC;

@Mod(SealDice2MC.MOD_ID)
public final class SealDice2MCForge {
    public SealDice2MCForge() {
        EventBuses.registerModEventBus(SealDice2MC.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SealDice2MC.init();
    }
}
