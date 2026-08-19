package com.velddev.xpalchemy.mixins;

import com.velddev.xpalchemy.access.XpDebtHearts;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Backs XpDebtHearts, separate from vanilla absorption. Consumption lives
// in PlayerXpDebtMixin since Player overrides actuallyHurt without super.
@Mixin(LivingEntity.class)
public abstract class LivingEntityXpDebtMixin implements XpDebtHearts {

    @Unique
    private static final String XP_DEBT_HEARTS_TAG = "XpDebtHearts";

    @Unique
    private static final EntityDataAccessor<Float> XPALCHEMY$DATA_XP_DEBT_HEARTS = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void xpalchemy$defineXpDebtHearts(CallbackInfo ci) {
        ((LivingEntity) (Object) this).getEntityData().define(XPALCHEMY$DATA_XP_DEBT_HEARTS, 0.0F);
    }

    @Override
    public float xpalchemy$getXpDebtHearts() {
        return ((LivingEntity) (Object) this).getEntityData().get(XPALCHEMY$DATA_XP_DEBT_HEARTS);
    }

    @Override
    public void xpalchemy$setXpDebtHearts(float amount) {
        ((LivingEntity) (Object) this).getEntityData().set(XPALCHEMY$DATA_XP_DEBT_HEARTS, Math.max(amount, 0.0F));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void xpalchemy$saveXpDebtHearts(CompoundTag compound, CallbackInfo ci) {
        compound.putFloat(XP_DEBT_HEARTS_TAG, this.xpalchemy$getXpDebtHearts());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void xpalchemy$readXpDebtHearts(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains(XP_DEBT_HEARTS_TAG, Tag.TAG_FLOAT)) {
            this.xpalchemy$setXpDebtHearts(compound.getFloat(XP_DEBT_HEARTS_TAG));
        }
    }
}
