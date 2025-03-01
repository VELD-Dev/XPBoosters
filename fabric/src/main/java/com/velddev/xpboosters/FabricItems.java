package com.velddev.xpboosters;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

public class FabricItems {
    public static void RegisterPotions() {
        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Constants.MOD_ID, XPPotions.XP_BOOST_POTION_LVL1_ID), XPPotions.XP_BOOST_POTION_LVL1);
        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Constants.MOD_ID, XPPotions.XP_BOOST_POTION_LVL2_ID), XPPotions.XP_BOOST_POTION_LVL2);
        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Constants.MOD_ID, XPPotions.XP_BOOST_POTION_LVL3_ID), XPPotions.XP_BOOST_POTION_LVL3);
        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Constants.MOD_ID, XPPotions.XP_BOOST_POTION_LVL4_ID), XPPotions.XP_BOOST_POTION_LVL4);


        FabricBrewingRecipeRegistry.registerPotionRecipe(
                Potions.THICK,
                Ingredient.of(net.minecraft.world.item.Items.GLOW_BERRIES),
                XPPotions.XP_BOOST_POTION_LVL1);
        FabricBrewingRecipeRegistry.registerPotionRecipe(
                XPPotions.XP_BOOST_POTION_LVL1,
                Ingredient.of(net.minecraft.world.item.Items.GHAST_TEAR),
                XPPotions.XP_BOOST_POTION_LVL2);
        FabricBrewingRecipeRegistry.registerPotionRecipe(
                XPPotions.XP_BOOST_POTION_LVL2,
                Ingredient.of(net.minecraft.world.item.Items.EXPERIENCE_BOTTLE),
                XPPotions.XP_BOOST_POTION_LVL3);
        FabricBrewingRecipeRegistry.registerPotionRecipe(
                XPPotions.XP_BOOST_POTION_LVL3,
                Ingredient.of(net.minecraft.world.item.Items.NETHERITE_SCRAP),
                XPPotions.XP_BOOST_POTION_LVL4);
    }
}
