package fr.kevyn.artisanspath.event;

import com.mojang.blaze3d.platform.InputConstants;
import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.block.ATMBlockEntity;
import fr.kevyn.artisanspath.init.ArtisansBlockEntities;
import fr.kevyn.artisanspath.init.ArtisansBlocks;
import fr.kevyn.artisanspath.init.ArtisansEntities;
import fr.kevyn.artisanspath.init.ArtisansItems;
import fr.kevyn.artisanspath.init.ArtisansMenus;
import fr.kevyn.artisanspath.item.PaymentCardItem;
import fr.kevyn.artisanspath.renderer.CoinProjectileRenderer;
import fr.kevyn.artisanspath.renderer.RedstonePaygateRenderer;
import fr.kevyn.artisanspath.ui.screen.ATMScreen;
import fr.kevyn.artisanspath.ui.screen.RedstonePaygateScreen;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(
    modid = ArtisansMod.MODID,
    bus = EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT)
public class ModClientEvent {
  private ModClientEvent() {}

  @SubscribeEvent
  public static void registerScreens(RegisterMenuScreensEvent event) {
    event.register(ArtisansMenus.ATM_MENU.get(), ATMScreen::new);
    event.register(ArtisansMenus.REDSTONE_PAYGATE_MENU.get(), RedstonePaygateScreen::new);
  }

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(
        ArtisansEntities.COIN_PROJECTILE.get(), CoinProjectileRenderer::new);
    event.registerBlockEntityRenderer(
        ArtisansBlockEntities.REDSTONE_PAYGATE.get(), RedstonePaygateRenderer::new);
  }

  public static final Lazy<KeyMapping> ACCOUNTS_MANAGER_MAPPING = Lazy.of(() -> new KeyMapping(
      "key." + ArtisansMod.MODID + ".accounts_manager",
      InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_P,
      "key.categories.misc"));

  @SubscribeEvent
  public static void registerBindings(RegisterKeyMappingsEvent event) {
    event.register(ACCOUNTS_MANAGER_MAPPING.get());
  }

  @SubscribeEvent
  public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
    event.register(PaymentCardItem::getColor, ArtisansItems.PAYMENT_CARD.get());
  }

  @SubscribeEvent
  public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
    event.register(
        (state, level, pos, tintIndex) -> {
          if (tintIndex != 0) return -1;
          if (level != null
              && pos != null
              && level.getBlockEntity(pos) instanceof ATMBlockEntity atm) {
            int color = atm.getColor();
            return color != ATMBlockEntity.DEFAULT_COLOR ? color : 0xFFFFFFFF;
          }
          return 0xFFFFFFFF;
        },
        ArtisansBlocks.ATM.get());
  }
}
