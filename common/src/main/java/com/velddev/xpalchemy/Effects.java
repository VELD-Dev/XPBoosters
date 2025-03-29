package com.velddev.xpalchemy;

import com.velddev.xpalchemy.effects.XPBoostEffect;
import com.velddev.xpalchemy.effects.XPDebtEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class Effects {
    public static final String XP_BOOST_EFFECT_ID = "xp_boost_effect";
    public static final String XP_HEALTH_DEBT_EFFECT_ID = "xp_health_debt_effect";
    public static final MobEffect XP_BOOST_EFFECT = new XPBoostEffect(MobEffectCategory.BENEFICIAL, 0x90E821);
    public static final MobEffect XP_HEALTH_DEBT = new XPDebtEffect(MobEffectCategory.BENEFICIAL, 0xFFB540);
}
