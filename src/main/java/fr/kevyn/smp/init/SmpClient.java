package fr.kevyn.smp.init;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.renderer.CoinProjectileRenderer;
import fr.kevyn.smp.renderer.RedstonePaygateRenderer;
import fr.kevyn.smp.ui.screen.ATMScreen;
import fr.kevyn.smp.ui.screen.RedstonePaygateScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = SmpMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SmpClient {
  private SmpClient() {}

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
}
