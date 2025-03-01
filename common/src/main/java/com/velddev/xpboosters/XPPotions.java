package com.velddev.xpboosters;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class XPPotions {
    public static final String XP_BOOST_POTION_LVL1_ID = "xp_boost_potion_1";
    public static final String XP_BOOST_POTION_LVL2_ID = "xp_boost_potion_2";
    public static final String XP_BOOST_POTION_LVL3_ID = "xp_boost_potion_3";
    public static final String XP_BOOST_POTION_LVL4_ID = "xp_boost_potion_4";

    public static final Potion XP_BOOST_POTION_LVL1 = new Potion(new MobEffectInstance(Effects.XP_BOOST_EFFECT, 6000, 0));
    public static final Potion XP_BOOST_POTION_LVL2 = new Potion(new MobEffectInstance(Effects.XP_BOOST_EFFECT, 12000, 0));
    public static final Potion XP_BOOST_POTION_LVL3 = new Potion(new MobEffectInstance(Effects.XP_BOOST_EFFECT, 18000, 1));
    public static final Potion XP_BOOST_POTION_LVL4 = new Potion(new MobEffectInstance(Effects.XP_BOOST_EFFECT, 18000, 2));
}
