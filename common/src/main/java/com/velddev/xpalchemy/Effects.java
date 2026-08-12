package com.velddev.xpalchemy;

import com.velddev.xpalchemy.effects.XPBoostEffect;
import com.velddev.xpalchemy.effects.XPDebtHealthEffect;
import com.velddev.xpalchemy.effects.XPDebtFoodEffect;
import com.velddev.xpalchemy.effects.XPDebtStrengthEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class Effects {
    public static final String XP_BOOST_EFFECT_ID = "xp_boost_effect";
    public static final String XP_HEALTH_DEBT_EFFECT_ID = "xp_health_debt_effect";
    public static final String XP_STRENGTH_DEBT_EFFECT_ID = "xp_strength_debt_effect";
    public static final String XP_FOOD_DEBT_EFFECT_ID = "xp_food_debt_effect";

    public static final MobEffect XP_BOOST_EFFECT = new XPBoostEffect(MobEffectCategory.BENEFICIAL, 0x90E821);
    public static final XPDebtHealthEffect XP_HEALTH_DEBT = new XPDebtHealthEffect(MobEffectCategory.BENEFICIAL, 0xFFB540);
    public static final XPDebtStrengthEffect XP_STRENGTH_DEBT = new XPDebtStrengthEffect(MobEffectCategory.BENEFICIAL, 0xFF6633);
    public static final XPDebtFoodEffect XP_FOOD_DEBT = new XPDebtFoodEffect(MobEffectCategory.BENEFICIAL, 0xFFCC33);
}
