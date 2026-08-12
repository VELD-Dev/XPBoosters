package com.velddev.xpalchemy.access;

// Implemented by Player via mixin. Backs the three debt pools (HP, Strength,
// Food) that talismans borrow against. Kept off LivingEntity since debt is an
// XP-driven, player-only concept - unlike XpDebtHearts, which any living
// entity could technically carry.
public interface PlayerDebt {
    float xpalchemy$getHpDebt();
    void xpalchemy$setHpDebt(float value);

    float xpalchemy$getStrengthDebt();
    void xpalchemy$setStrengthDebt(float value);

    float xpalchemy$getFoodDebt();
    void xpalchemy$setFoodDebt(float value);
}
