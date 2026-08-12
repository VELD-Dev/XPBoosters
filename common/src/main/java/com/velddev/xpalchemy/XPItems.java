package com.velddev.xpalchemy;

import com.velddev.xpalchemy.items.XPDebtFoodItem;
import com.velddev.xpalchemy.items.XPDebtHPItem;
import com.velddev.xpalchemy.items.XPDebtStrengthItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class XPItems {
    public static final String XP_DEBT_CRYSTAL_HP_ID = "xp_debt_crystal_hp";
    public static final String XP_DEBT_CRYSTAL_STRENGTH_ID = "xp_debt_crystal_strength";
    public static final String XP_DEBT_CRYSTAL_FOOD_ID = "xp_debt_crystal_food";
    public static final String XP_CORE_ID = "xpcore";

    public static final Item XP_DEBT_CRYSTAL_HP = new XPDebtHPItem(new Item.Properties().rarity(Rarity.RARE).fireResistant().stacksTo(1));
    public static final Item XP_DEBT_CRYSTAL_STRENGTH = new XPDebtStrengthItem(new Item.Properties().rarity(Rarity.RARE).fireResistant().stacksTo(1));
    public static final Item XP_DEBT_CRYSTAL_FOOD = new XPDebtFoodItem(new Item.Properties().rarity(Rarity.RARE).fireResistant().stacksTo(1));
    public static final Item XP_CORE = new Item(new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant().stacksTo(4));
}
