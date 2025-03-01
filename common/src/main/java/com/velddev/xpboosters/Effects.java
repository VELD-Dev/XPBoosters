package com.velddev.xpboosters;

import com.velddev.xpboosters.effects.XPBoostEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class Effects {
    public static final String XP_BOOST_EFFECT_ID = "xp_boost_effect";
    public static final MobEffect XP_BOOST_EFFECT = new XPBoostEffect(MobEffectCategory.BENEFICIAL, 0x989821);
}
