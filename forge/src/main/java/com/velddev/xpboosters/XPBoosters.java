package com.velddev.xpboosters;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.spongepowered.asm.mixin.Mixins;

@Mod(Constants.MOD_ID)
public class XPBoosters {
    
    public XPBoosters() {
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        Constants.LOGGER.info("Hello Forge world!");
        CommonMain.init();

        Mixins.addConfiguration("xpboosters.mixins.json");
        //Mixins.addConfiguration("xpboosters.forge.mixins.json");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        ForgeEffects.RegisterEffects(modEventBus);
        ForgeItems.RegisterItems(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ForgeItems.RegisterBrewingRecipes(event);
    }
}