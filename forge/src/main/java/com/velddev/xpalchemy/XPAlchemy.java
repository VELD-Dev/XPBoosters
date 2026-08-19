package com.velddev.xpalchemy;

import com.velddev.xpalchemy.commands.XPAlchemyCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.spongepowered.asm.mixin.Mixins;

@Mod(Constants.MOD_ID)
public class XPAlchemy {

    public XPAlchemy() {
        Constants.LOGGER.info("Hello Forge world!");
        CommonMain.init();

        Mixins.addConfiguration("xpboosters.mixins.json");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(ForgeItems::RegisterCreativeTabItems);
        MinecraftForge.EVENT_BUS.register(this);

        ForgeEffects.RegisterEffects(modEventBus);
        ForgeItems.RegisterItems(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ForgeItems.RegisterBrewingRecipes(event);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        XPAlchemyCommand.register(event.getDispatcher());
    }
}
