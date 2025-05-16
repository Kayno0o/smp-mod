package fr.kevyn.economy;

// import org.slf4j.Logger;

// import com.mojang.logging.LogUtils;

import fr.kevyn.economy.init.BlockEntities;
import fr.kevyn.economy.init.Blocks;
import fr.kevyn.economy.init.Items;
import fr.kevyn.economy.init.Tabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EconomyMod.MODID)
public class EconomyMod {
  // Define mod id in a common place for everything to reference
  public static final String MODID = "economy";
  // Directly reference a slf4j logger
  // private static final Logger LOGGER = LogUtils.getLogger();
  // Create a Deferred Register to hold CreativeModeTabs which will all be
  // registered under the "economy" namespace
  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
      .create(Registries.CREATIVE_MODE_TAB, MODID);

  // The constructor for the mod class is the first code that is run when your mod
  // is loaded.
  // FML will recognize some parameter types like IEventBus or ModContainer and
  // pass them in automatically.
  public EconomyMod(IEventBus modEventBus, ModContainer modContainer) {
    Items.REGISTRY.register(modEventBus);
    Blocks.REGISTRY.register(modEventBus);
    BlockEntities.REGISTRY.register(modEventBus);
    Tabs.REGISTRY.register(modEventBus);
  }
}
