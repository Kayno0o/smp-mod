package fr.kevyn.smp;

import com.mojang.logging.LogUtils;
import fr.kevyn.smp.init.SmpBlockEntities;
import fr.kevyn.smp.init.SmpBlocks;
import fr.kevyn.smp.init.SmpComponents;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.init.SmpEntities;
import fr.kevyn.smp.init.SmpItems;
import fr.kevyn.smp.init.SmpMenus;
import fr.kevyn.smp.init.SmpTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(SmpMod.MODID)
public class SmpMod {
  public static final String MODID = "smp";
  public static final Logger LOGGER = LogUtils.getLogger();

  public SmpMod(IEventBus modEventBus, ModContainer modContainer) {
    LOGGER.info("Smp Mod Initializing");

    SmpItems.REGISTRY.register(modEventBus);
    SmpBlocks.REGISTRY.register(modEventBus);
    SmpBlockEntities.REGISTRY.register(modEventBus);
    SmpTabs.REGISTRY.register(modEventBus);
    SmpMenus.REGISTRY.register(modEventBus);
    SmpComponents.REGISTRY.register(modEventBus);
    SmpDataAttachments.REGISTRY.register(modEventBus);
    SmpEntities.REGISTRY.register(modEventBus);

    LOGGER.info("Smp Mod Initialized");
  }
}
