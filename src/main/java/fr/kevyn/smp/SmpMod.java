package fr.kevyn.smp;

import fr.kevyn.smp.atm.ATMScreen;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import fr.kevyn.smp.init.BlockEntities;
import fr.kevyn.smp.init.Blocks;
import fr.kevyn.smp.init.DataAttachment;
import fr.kevyn.smp.init.ModComponents;
import fr.kevyn.smp.init.Items;
import fr.kevyn.smp.init.Menus;
import fr.kevyn.smp.init.Tabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(SmpMod.MODID)
public class SmpMod {
  public static final String MODID = "smp";
  public static final Logger LOGGER = LogUtils.getLogger();

  public SmpMod(IEventBus modEventBus, ModContainer modContainer) {
    LOGGER.info("Smp Mod Initializing");

    Items.REGISTRY.register(modEventBus);
    Blocks.REGISTRY.register(modEventBus);
    BlockEntities.REGISTRY.register(modEventBus);
    Tabs.REGISTRY.register(modEventBus);
    Menus.REGISTRY.register(modEventBus);
    ModComponents.REGISTRY.register(modEventBus);
    DataAttachment.REGISTRY.register(modEventBus);

    LOGGER.info("Smp Mod Initialized");
  }

  @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
  public static class ClientModEvents {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
      event.register(Menus.ATM_MENU.get(), ATMScreen::new);
    }
  }
}
