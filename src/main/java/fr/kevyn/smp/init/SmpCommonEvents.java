package fr.kevyn.smp.init;

import fr.kevyn.smp.SmpMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = SmpMod.MODID)
public class SmpCommonEvents {
  @SubscribeEvent
  public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
    var player = event.getEntity();

    // Trigger loading of data from NBT, or create if missing
    int money = player.getData(SmpDataAttachments.MONEY);
    player.setData(SmpDataAttachments.MONEY, money); // triggers hydration if needed
  }

  @SubscribeEvent
  public static void onPlayerClone(PlayerEvent.Clone event) {
    var original = event.getOriginal();
    var clone = event.getEntity();

    int money = original.getData(SmpDataAttachments.MONEY);
    clone.setData(SmpDataAttachments.MONEY, money); // copy data manually
  }
}
