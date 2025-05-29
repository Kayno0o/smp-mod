package fr.kevyn.smp.event;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.data.AccountEntry;
import fr.kevyn.smp.data.ServerAccountManager;
import fr.kevyn.smp.utils.AccountUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent.EntityPlaceEvent;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

@EventBusSubscriber(modid = SmpMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvent {
  private GameEvent() {}

  @SubscribeEvent
  public static void onBlockPlaced(EntityPlaceEvent event) {
    if (event.getEntity() instanceof Player player) {
      BlockPos pos = event.getPos();
      BlockEntity blockEntity = event.getLevel().getBlockEntity(pos);

      if (blockEntity instanceof CookingPotBlockEntity && blockEntity.getLevel() instanceof Level) {
        blockEntity.getPersistentData().putUUID("smp:owner", player.getUUID());
      }
    }
  }

  @SubscribeEvent
  public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) return;

    ServerLevel level = player.serverLevel();
    UUID playerId = player.getUUID();

    if (!ServerAccountManager.hasAccount(level, playerId)) {
      Map<UUID, String> allowedAccess = new HashMap<>();
      allowedAccess.put(playerId, player.getName().getString());

      AccountEntry newAccount =
          new AccountEntry(playerId, playerId, player.getName().getString(), 0, allowedAccess);
      ServerAccountManager.putAccount(level, playerId, newAccount);
    }

    AccountUtils.notifyPlayer(level, playerId);
  }
}
