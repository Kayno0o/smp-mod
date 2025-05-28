package fr.kevyn.smp.event;

import com.mojang.blaze3d.platform.InputConstants;
import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.init.SmpBlockEntities;
import fr.kevyn.smp.init.SmpEntities;
import fr.kevyn.smp.init.SmpMenus;
import fr.kevyn.smp.renderer.CoinProjectileRenderer;
import fr.kevyn.smp.renderer.RedstonePaygateRenderer;
import fr.kevyn.smp.ui.screen.ATMScreen;
import fr.kevyn.smp.ui.screen.RedstonePaygateScreen;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = SmpMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvent {
  private ModClientEvent() {}

  @SubscribeEvent
  public static void registerScreens(RegisterMenuScreensEvent event) {
    event.register(SmpMenus.ATM_MENU.get(), ATMScreen::new);
    event.register(SmpMenus.REDSTONE_PAYGATE_MENU.get(), RedstonePaygateScreen::new);
  }

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(SmpEntities.COIN_PROJECTILE.get(), CoinProjectileRenderer::new);
    event.registerBlockEntityRenderer(
        SmpBlockEntities.REDSTONE_PAYGATE.get(), RedstonePaygateRenderer::new);
  }

  public static final Lazy<KeyMapping> ACCOUNTS_MANAGER_MAPPING = Lazy.of(() -> new KeyMapping(
      "key." + SmpMod.MODID + ".accounts_manager",
      InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_P,
      "key.categories.misc"));

  @SubscribeEvent
  public static void registerBindings(RegisterKeyMappingsEvent event) {
    event.register(ACCOUNTS_MANAGER_MAPPING.get());
  }
}
