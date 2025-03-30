package com.velddev.xpalchemy;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

public class FabricItems {
    public static void RegisterPotions() {
        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Constants.MOD_ID, XPPotions.XP_BOOST_POTION_LVL1_ID), XPPotions.XP_BOOST_POTION_LVL1);
        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Constants.MOD_ID, XPPotions.XP_BOOST_POTION_LVL2_ID), XPPotions.XP_BOOST_POTION_LVL2);
        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Constants.MOD_ID, XPPotions.XP_BOOST_POTION_LVL3_ID), XPPotions.XP_BOOST_POTION_LVL3);
        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Constants.MOD_ID, XPPotions.XP_BOOST_POTION_LVL4_ID), XPPotions.XP_BOOST_POTION_LVL4);

        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Constants.MOD_ID, XPPotions.XP_HEALTH_DEBT_LVL1_ID), XPPotions.XP_HEALTH_DEBT_LVL1);
        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Constants.MOD_ID, XPPotions.XP_HEALTH_DEBT_LVL2_ID), XPPotions.XP_HEALTH_DEBT_LVL2);
        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Constants.MOD_ID, XPPotions.XP_HEALTH_DEBT_LVL3_ID), XPPotions.XP_HEALTH_DEBT_LVL3);

        // XP Boost potions brewing recipes
        FabricBrewingRecipeRegistry.registerPotionRecipe(
                Potions.THICK,
                Ingredient.of(Items.GLOW_BERRIES),
                XPPotions.XP_BOOST_POTION_LVL1);
        FabricBrewingRecipeRegistry.registerPotionRecipe(
                XPPotions.XP_BOOST_POTION_LVL1,
                Ingredient.of(Items.GHAST_TEAR),
                XPPotions.XP_BOOST_POTION_LVL2);
        FabricBrewingRecipeRegistry.registerPotionRecipe(
                XPPotions.XP_BOOST_POTION_LVL2,
                Ingredient.of(Items.EXPERIENCE_BOTTLE),
                XPPotions.XP_BOOST_POTION_LVL3);
        FabricBrewingRecipeRegistry.registerPotionRecipe(
                XPPotions.XP_BOOST_POTION_LVL3,
                Ingredient.of(Items.NETHERITE_SCRAP),
                XPPotions.XP_BOOST_POTION_LVL4);

        // Health Debt potions brewing recipes
        FabricBrewingRecipeRegistry.registerPotionRecipe(
                Potions.THICK,
                Ingredient.of(Items.NAUTILUS_SHELL),
                XPPotions.XP_HEALTH_DEBT_LVL1
        );
        FabricBrewingRecipeRegistry.registerPotionRecipe(
                XPPotions.XP_HEALTH_DEBT_LVL1,
                Ingredient.of(Items.ECHO_SHARD),
                XPPotions.XP_HEALTH_DEBT_LVL2
        );
        FabricBrewingRecipeRegistry.registerPotionRecipe(
                XPPotions.XP_HEALTH_DEBT_LVL2,
                Ingredient.of(Items.HEART_OF_THE_SEA),
                XPPotions.XP_HEALTH_DEBT_LVL3
        );
    }
}
