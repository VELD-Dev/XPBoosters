package com.velddev.xpboosters;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Items {
    public static Potion XP_BOOST_POTION_LVL1;
    public static Potion XP_BOOST_POTION_LVL2;
    public static Potion XP_BOOST_POTION_LVL3;

    public static void RegisterPotions() {
        XP_BOOST_POTION_LVL1 = new Potion(new StatusEffectInstance(Effects.XP_BOOST_EFFECT, 6000, 0));
        XP_BOOST_POTION_LVL2 = new Potion(new StatusEffectInstance(Effects.XP_BOOST_EFFECT, 9000, 0));
        XP_BOOST_POTION_LVL3 = new Potion(new StatusEffectInstance(Effects.XP_BOOST_EFFECT, 18000, 1));

        Registry.register(Registries.POTION, new Identifier(Main.MOD_ID, "xp_boost_potion_1"), XP_BOOST_POTION_LVL1);
        Registry.register(Registries.POTION, new Identifier(Main.MOD_ID, "xp_boost_potion_2"), XP_BOOST_POTION_LVL2);
        Registry.register(Registries.POTION, new Identifier(Main.MOD_ID, "xp_boost_potion_3"), XP_BOOST_POTION_LVL3);


        BrewingRecipeRegistry.registerPotionRecipe(Potions.THICK, net.minecraft.item.Items.GLOW_BERRIES, XP_BOOST_POTION_LVL1);
        BrewingRecipeRegistry.registerPotionRecipe(XP_BOOST_POTION_LVL1, net.minecraft.item.Items.GHAST_TEAR, XP_BOOST_POTION_LVL2);
        BrewingRecipeRegistry.registerPotionRecipe(XP_BOOST_POTION_LVL2, net.minecraft.item.Items.EXPERIENCE_BOTTLE, XP_BOOST_POTION_LVL3);
    }
}
