package fr.kevyn.artisanspath;

import com.mojang.logging.LogUtils;
import fr.kevyn.artisanspath.init.ArtisansBlockEntities;
import fr.kevyn.artisanspath.init.ArtisansBlocks;
import fr.kevyn.artisanspath.init.ArtisansComponents;
import fr.kevyn.artisanspath.init.ArtisansDataAttachments;
import fr.kevyn.artisanspath.init.ArtisansEntities;
import fr.kevyn.artisanspath.init.ArtisansItems;
import fr.kevyn.artisanspath.init.ArtisansMenus;
import fr.kevyn.artisanspath.init.ArtisansTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ArtisansMod.MODID)
public class ArtisansMod {
  public static final String MODID = "artisanspath";
  public static final Logger LOGGER = LogUtils.getLogger();

  public static final int LABEL_COLOR = 0x253b41;

  public ArtisansMod(IEventBus modEventBus, ModContainer modContainer) {
    LOGGER.info("Artisans Mod Initializing");

    ArtisansItems.REGISTRY.register(modEventBus);
    ArtisansBlocks.REGISTRY.register(modEventBus);
    ArtisansBlockEntities.REGISTRY.register(modEventBus);
    ArtisansTabs.REGISTRY.register(modEventBus);
    ArtisansMenus.REGISTRY.register(modEventBus);
    ArtisansComponents.REGISTRY.register(modEventBus);
    ArtisansDataAttachments.REGISTRY.register(modEventBus);
    ArtisansEntities.REGISTRY.register(modEventBus);

    LOGGER.info("Artisans Mod Initialized");
  }
}
