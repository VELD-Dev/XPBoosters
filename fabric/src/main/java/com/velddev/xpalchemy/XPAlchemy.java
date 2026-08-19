package com.velddev.xpalchemy;

import com.velddev.xpalchemy.commands.XPAlchemyCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class XPAlchemy implements ModInitializer {

    @Override
    public void onInitialize() {

        Constants.LOGGER.info("Loading {} [{}] (v{})", Constants.MOD_NAME, Constants.MOD_ID, Constants.MOD_VERSION);
        CommonMain.init();

        FabricEffects.RegisterEffects();
        FabricItems.RegisterPotions();
        FabricItems.RegisterItems();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> XPAlchemyCommand.register(dispatcher));
    }
}
