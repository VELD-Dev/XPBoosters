package com.velddev.xpalchemy.access;

// Implemented by Player via mixin, backs the three debt pools
public interface PlayerDebt {
    float xpalchemy$getHpDebt();
    void xpalchemy$setHpDebt(float value);

    float xpalchemy$getStrengthDebt();
    void xpalchemy$setStrengthDebt(float value);

    float xpalchemy$getFoodDebt();
    void xpalchemy$setFoodDebt(float value);
}
