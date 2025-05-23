package fr.kevyn.smp.init;

import fr.kevyn.smp.SmpMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent.EntityPlaceEvent;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

@EventBusSubscriber(modid = SmpMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class SmpServer {
  @SubscribeEvent
  public static void onBlockPlaced(EntityPlaceEvent event) {
    if (event.getEntity() instanceof Player player) {
      BlockPos pos = event.getPos();
      BlockEntity blockEntity = event.getLevel().getBlockEntity(pos);

      if (blockEntity != null
          && blockEntity instanceof CookingPotBlockEntity
          && blockEntity.getLevel() instanceof Level) {
        blockEntity.getPersistentData().putUUID("smp:owner", player.getUUID());
      }
    }
  }
}
