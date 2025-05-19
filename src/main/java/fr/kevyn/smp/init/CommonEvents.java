package fr.kevyn.smp.init;

import fr.kevyn.smp.SmpMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = SmpMod.MODID)
public class CommonEvents {
  @SubscribeEvent
  public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
    var player = event.getEntity();

    // Trigger loading of data from NBT, or create if missing
    int money = player.getData(DataAttachment.MONEY);
    player.setData(DataAttachment.MONEY, money); // triggers hydration if needed
  }

  @SubscribeEvent
  public static void onPlayerClone(PlayerEvent.Clone event) {
    var original = event.getOriginal();
    var clone = event.getEntity();

    int money = original.getData(DataAttachment.MONEY);
    clone.setData(DataAttachment.MONEY, money); // copy data manually
  }
}
