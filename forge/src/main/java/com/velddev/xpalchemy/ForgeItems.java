package com.velddev.xpalchemy;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;

public class ForgeItems {
    private static final DeferredRegister<Potion> POTIONS_REGISTRY = DeferredRegister.create(Registries.POTION, Constants.MOD_ID);
    private static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(Registries.ITEM, Constants.MOD_ID);

    public static void RegisterItems(IEventBus eventBus) {
        POTIONS_REGISTRY.register(eventBus);
        ITEMS_REGISTRY.register(eventBus);

        POTIONS_REGISTRY.register(XPPotions.XP_BOOST_POTION_LVL1_ID, () -> XPPotions.XP_BOOST_POTION_LVL1);
        POTIONS_REGISTRY.register(XPPotions.XP_BOOST_POTION_LVL2_ID, () -> XPPotions.XP_BOOST_POTION_LVL2);
        POTIONS_REGISTRY.register(XPPotions.XP_BOOST_POTION_LVL3_ID, () -> XPPotions.XP_BOOST_POTION_LVL3);
        POTIONS_REGISTRY.register(XPPotions.XP_BOOST_POTION_LVL4_ID, () -> XPPotions.XP_BOOST_POTION_LVL4);

        ITEMS_REGISTRY.register(XPItems.XP_DEBT_CRYSTAL_PV_ID, () -> XPItems.XP_DEBT_CRYSTAL_1);
        ITEMS_REGISTRY.register(XPItems.XP_DEBT_CRYSTAL_STRENGTH_ID, () -> XPItems.XP_DEBT_CRYSTAL_2);
        ITEMS_REGISTRY.register(XPItems.XP_DEBT_CRYSTAL_FOOD_ID, () -> XPItems.XP_DEBT_CRYSTAL_3);
    }

    public static void RegisterBrewingRecipes(FMLCommonSetupEvent event) {
        PotionBrewing.addMix(Potions.THICK, Items.GLOW_BERRIES, XPPotions.XP_BOOST_POTION_LVL1);
        PotionBrewing.addMix(XPPotions.XP_BOOST_POTION_LVL1, Items.GHAST_TEAR, XPPotions.XP_BOOST_POTION_LVL2);
        PotionBrewing.addMix(XPPotions.XP_BOOST_POTION_LVL2, Items.EXPERIENCE_BOTTLE, XPPotions.XP_BOOST_POTION_LVL3);
        PotionBrewing.addMix(XPPotions.XP_BOOST_POTION_LVL3, Items.NETHERITE_SCRAP, XPPotions.XP_BOOST_POTION_LVL4);
    }

    public static void RegisterCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(XPItems.XP_DEBT_CRYSTAL_1);
            event.accept(XPItems.XP_DEBT_CRYSTAL_2);
            event.accept(XPItems.XP_DEBT_CRYSTAL_3);
        }
    }
}
