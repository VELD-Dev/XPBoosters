package com.velddev.xpalchemy.mixins;

import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// HP debt counter-effect: successfully breaking a block pays back 0.1 debt.
// Creative players don't pay it back this way (they never really "worked"
// for it), matching the instabuild guard used elsewhere in this system.
@Mixin(ServerPlayerGameMode.class)
public abstract class BlockBreakDebtMixin {

    @Shadow
    protected ServerPlayer player;

    @Inject(method = "destroyBlock", at = @At("RETURN"))
    private void xpalchemy$reduceHpDebtOnBlockBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && !this.player.getAbilities().instabuild) {
            PlayerDebtData.reduceDebt(this.player, PlayerDebtData.DebtType.HP, 0.1F);
        }
    }
}
