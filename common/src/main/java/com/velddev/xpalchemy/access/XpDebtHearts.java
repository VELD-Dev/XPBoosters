package com.velddev.xpalchemy.access;

// Implemented by LivingEntity via mixin. Kept entirely separate from vanilla
// absorption (LivingEntity#getAbsorptionAmount) so XP Debt hearts can be
// tracked, drained and refreshed independently of absorption granted by other
// effects/items.
public interface XpDebtHearts {
    float xpalchemy$getXpDebtHearts();

    void xpalchemy$setXpDebtHearts(float amount);
}
