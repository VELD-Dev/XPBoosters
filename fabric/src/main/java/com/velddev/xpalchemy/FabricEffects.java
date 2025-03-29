package com.velddev.xpalchemy;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class FabricEffects {
    public static void RegisterEffects() {
        Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(Constants.MOD_ID, Effects.XP_BOOST_EFFECT_ID), Effects.XP_BOOST_EFFECT);
        Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(Constants.MOD_ID, Effects.XP_HEALTH_DEBT_EFFECT_ID), Effects.XP_HEALTH_DEBT);
    }
}
