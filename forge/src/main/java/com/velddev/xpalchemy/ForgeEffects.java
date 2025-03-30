package com.velddev.xpalchemy;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgeEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS_REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Constants.MOD_ID);

    public static void RegisterEffects(IEventBus eventBus) {
        MOB_EFFECTS_REGISTRY.register(eventBus);
        MOB_EFFECTS_REGISTRY.register(Effects.XP_BOOST_EFFECT_ID, () -> Effects.XP_BOOST_EFFECT);
        MOB_EFFECTS_REGISTRY.register(Effects.XP_HEALTH_DEBT_EFFECT_ID, () -> Effects.XP_HEALTH_DEBT);
    }
}
