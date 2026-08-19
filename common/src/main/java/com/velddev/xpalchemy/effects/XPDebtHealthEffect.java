package com.velddev.xpalchemy.effects;

import com.velddev.xpalchemy.access.XpDebtHearts;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class XPDebtHealthEffect extends MobEffect {

    public XPDebtHealthEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    // Heals to full and grants XP Debt hearts additively.
    public void grantHearts(Player player, float amount) {
        if (amount <= 0.0F) {
            return;
        }

        player.heal(player.getMaxHealth() - player.getHealth());
        XpDebtHearts hearts = (XpDebtHearts) player;
        hearts.xpalchemy$setXpDebtHearts(hearts.xpalchemy$getXpDebtHearts() + amount);

        if (!player.hasEffect(this)) {
            player.addEffect(new MobEffectInstance(this, -1, 0, false, true, true));
        }
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(!livingEntity.hasEffect(this))
            return;

        if(((XpDebtHearts) livingEntity).xpalchemy$getXpDebtHearts() < 0.2f) {
            livingEntity.removeEffect(this);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }
}
