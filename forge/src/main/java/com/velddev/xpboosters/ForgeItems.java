package com.velddev.xpboosters;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ForgeItems {
    private static final DeferredRegister<Potion> POTIONS_REGISTRY = DeferredRegister.create(Registries.POTION, Constants.MOD_ID);

    public static void RegisterItems(IEventBus eventBus) {
        POTIONS_REGISTRY.register(eventBus);

        POTIONS_REGISTRY.register(XPPotions.XP_BOOST_POTION_LVL1_ID, () -> XPPotions.XP_BOOST_POTION_LVL1);
        POTIONS_REGISTRY.register(XPPotions.XP_BOOST_POTION_LVL2_ID, () -> XPPotions.XP_BOOST_POTION_LVL2);
        POTIONS_REGISTRY.register(XPPotions.XP_BOOST_POTION_LVL3_ID, () -> XPPotions.XP_BOOST_POTION_LVL3);
        POTIONS_REGISTRY.register(XPPotions.XP_BOOST_POTION_LVL4_ID, () -> XPPotions.XP_BOOST_POTION_LVL4);
    }

    public static void RegisterBrewingRecipes(FMLCommonSetupEvent event) {
        PotionBrewing.addMix(Potions.THICK, Items.GLOW_BERRIES, XPPotions.XP_BOOST_POTION_LVL1);
        PotionBrewing.addMix(XPPotions.XP_BOOST_POTION_LVL1, Items.GHAST_TEAR, XPPotions.XP_BOOST_POTION_LVL2);
        PotionBrewing.addMix(XPPotions.XP_BOOST_POTION_LVL2, Items.EXPERIENCE_BOTTLE, XPPotions.XP_BOOST_POTION_LVL3);
        PotionBrewing.addMix(XPPotions.XP_BOOST_POTION_LVL3, Items.NETHERITE_SCRAP, XPPotions.XP_BOOST_POTION_LVL4);
    }
}
