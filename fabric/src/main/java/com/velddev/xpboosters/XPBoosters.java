package com.velddev.xpboosters;

import net.fabricmc.api.ModInitializer;

public class XPBoosters implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOGGER.info("Loading {} [{}] (v{})", Constants.MOD_NAME, Constants.MOD_ID, Constants.MOD_VERSION);
        CommonMain.init();

        FabricEffects.RegisterEffects();
        FabricItems.RegisterPotions();
    }
}
