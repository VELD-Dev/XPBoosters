package com.velddev.xpalchemy;

import com.velddev.xpalchemy.items.XPDebtItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class XPItems {
    public static final String XP_DEBT_CRYSTAL_PV_ID = "xp_debt_crystal_1";
    public static final String XP_DEBT_CRYSTAL_STRENGTH_ID = "xp_debt_crystal_2";
    public static final String XP_DEBT_CRYSTAL_FOOD_ID = "xp_debt_crystal_3";

    public static final Item XP_DEBT_CRYSTAL_1 = new XPDebtItem(0, new Item.Properties().rarity(Rarity.COMMON));
    public static final Item XP_DEBT_CRYSTAL_2 = new XPDebtItem(1, new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final Item XP_DEBT_CRYSTAL_3 = new XPDebtItem(2, new Item.Properties().rarity(Rarity.RARE));
}
