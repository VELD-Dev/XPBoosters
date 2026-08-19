package com.velddev.xpalchemy.mixins;

import com.velddev.xpalchemy.access.PlayerDebt;
import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Backs the three debt pools with SynchedEntityData, same as vanilla health
@Mixin(Player.class)
public abstract class PlayerDebtMixin implements PlayerDebt {

    @Unique
    private static final String HP_DEBT_TAG = "XpAlchemyHpDebt";
    @Unique
    private static final String STRENGTH_DEBT_TAG = "XpAlchemyStrengthDebt";
    @Unique
    private static final String FOOD_DEBT_TAG = "XpAlchemyFoodDebt";

    @Unique
    private static final EntityDataAccessor<Float> XPALCHEMY$DATA_HP_DEBT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);
    @Unique
    private static final EntityDataAccessor<Float> XPALCHEMY$DATA_STRENGTH_DEBT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);
    @Unique
    private static final EntityDataAccessor<Float> XPALCHEMY$DATA_FOOD_DEBT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);

    @Unique
    private int xpalchemy$foodDebtDecayTimer = 0;

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void xpalchemy$defineDebt(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        self.getEntityData().define(XPALCHEMY$DATA_HP_DEBT, 0.0F);
        self.getEntityData().define(XPALCHEMY$DATA_STRENGTH_DEBT, 0.0F);
        self.getEntityData().define(XPALCHEMY$DATA_FOOD_DEBT, 0.0F);
    }

    @Override
    public float xpalchemy$getHpDebt() {
        return ((Player) (Object) this).getEntityData().get(XPALCHEMY$DATA_HP_DEBT);
    }

    @Override
    public void xpalchemy$setHpDebt(float value) {
        ((Player) (Object) this).getEntityData().set(XPALCHEMY$DATA_HP_DEBT, Math.max(0.0F, Math.min(value, 100.0F)));
    }

    @Override
    public float xpalchemy$getStrengthDebt() {
        return ((Player) (Object) this).getEntityData().get(XPALCHEMY$DATA_STRENGTH_DEBT);
    }

    @Override
    public void xpalchemy$setStrengthDebt(float value) {
        ((Player) (Object) this).getEntityData().set(XPALCHEMY$DATA_STRENGTH_DEBT, Math.max(0.0F, Math.min(value, 100.0F)));
    }

    @Override
    public float xpalchemy$getFoodDebt() {
        return ((Player) (Object) this).getEntityData().get(XPALCHEMY$DATA_FOOD_DEBT);
    }

    @Override
    public void xpalchemy$setFoodDebt(float value) {
        ((Player) (Object) this).getEntityData().set(XPALCHEMY$DATA_FOOD_DEBT, Math.max(0.0F, Math.min(value, 100.0F)));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void xpalchemy$saveDebt(CompoundTag compound, CallbackInfo ci) {
        compound.putFloat(HP_DEBT_TAG, this.xpalchemy$getHpDebt());
        compound.putFloat(STRENGTH_DEBT_TAG, this.xpalchemy$getStrengthDebt());
        compound.putFloat(FOOD_DEBT_TAG, this.xpalchemy$getFoodDebt());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void xpalchemy$readDebt(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains(HP_DEBT_TAG, Tag.TAG_FLOAT)) {
            this.xpalchemy$setHpDebt(compound.getFloat(HP_DEBT_TAG));
        }
        if (compound.contains(STRENGTH_DEBT_TAG, Tag.TAG_FLOAT)) {
            this.xpalchemy$setStrengthDebt(compound.getFloat(STRENGTH_DEBT_TAG));
        }
        if (compound.contains(FOOD_DEBT_TAG, Tag.TAG_FLOAT)) {
            this.xpalchemy$setFoodDebt(compound.getFloat(FOOD_DEBT_TAG));
        }
    }

    // Food debt is the only type with passive decay (-0.5/sec)
    @Inject(method = "tick", at = @At("TAIL"))
    private void xpalchemy$tickFoodDebtDecay(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (self.level().isClientSide || self.isSpectator()) {
            return;
        }

        if (this.xpalchemy$getFoodDebt() <= 0.0F) {
            this.xpalchemy$foodDebtDecayTimer = 0;
            return;
        }

        this.xpalchemy$foodDebtDecayTimer++;
        if (this.xpalchemy$foodDebtDecayTimer >= 20) {
            this.xpalchemy$foodDebtDecayTimer = 0;
            PlayerDebtData.reduceDebt(self, PlayerDebtData.DebtType.FOOD, 0.5F);
        }
    }
}
