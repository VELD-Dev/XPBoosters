package com.velddev.xpboosters;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Main implements ModInitializer {

    public static final String MOD_ID = "xpboosters";
    public static final String MOD_VERSION = "0.1.0";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        LOGGER.info("Initializing {} (v{})...", MOD_ID, MOD_VERSION);

        Effects.RegisterEffects();
        LOGGER.info("Registered effects !");
        Items.RegisterPotions();
        LOGGER.info("Registered potions !");
    }
}
