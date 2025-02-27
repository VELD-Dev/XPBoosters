package com.velddev.xpboosters;

import com.velddev.xpboosters.effects.XPBoostEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class Effects {

    public static final StatusEffect XP_BOOST_EFFECT = new XPBoostEffect(StatusEffectCategory.BENEFICIAL, 0x989821);

    public static void RegisterEffects() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Main.MOD_ID, "xp_boost"), XP_BOOST_EFFECT);
    }
}
