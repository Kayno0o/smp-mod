package fr.kevyn.smp.utils;

import fr.kevyn.smp.component.AccountData;
import fr.kevyn.smp.component.LocalAccountEntry;
import fr.kevyn.smp.data.WorldAccountEntry;
import fr.kevyn.smp.data.WorldAccountManager;
import fr.kevyn.smp.init.SmpComponents;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.network.client.UpdatePlayerAccountsNet;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class AccountUtils {
  private AccountUtils() {}

  @Nullable public static UUID getAccountUUID(ItemStack stack) {
    if (stack.has(SmpComponents.OWNERSHIP)) {
      AccountData accountData = stack.get(SmpComponents.OWNERSHIP);
      if (accountData.hasAccount()) return accountData.accountUUID();
    }
    return null;
  }

  @Nullable public static WorldAccountEntry getAccount(Level level, UUID accountId) {
    if (!(level instanceof ServerLevel serverLevel)) return null;
    return WorldAccountManager.getAccount(serverLevel, accountId);
  }

  @Nullable public static WorldAccountEntry getAccount(Level level, ItemStack stack) {
    if (!(level instanceof ServerLevel serverLevel)) return null;

    var accountId = getAccountUUID(stack);
    if (accountId == null) return null;

    return WorldAccountManager.getAccount(serverLevel, accountId);
  }

  public static boolean hasAccessToAccount(ItemStack stack, ServerPlayer player, Level level) {
    UUID accountId = getAccountUUID(stack);
    if (accountId == null) return false;

    return hasAccessToAccount(accountId, player, level);
  }

  @Nullable public static LocalAccountEntry getAccount(ItemStack stack, LocalPlayer player) {
    if (!player.hasData(SmpDataAttachments.LOCAL_ACCOUNTS)) return null;

    var accountId = getAccountUUID(stack);
    if (accountId == null) return null;

    var accounts = player.getData(SmpDataAttachments.LOCAL_ACCOUNTS);
    if (!accounts.containsKey(accountId)) return null;

    return accounts.get(accountId);
  }

  public static int getMoney(ItemStack stack, LocalPlayer player) {
    if (!player.hasData(SmpDataAttachments.LOCAL_ACCOUNTS)) return -1;

    var accountId = getAccountUUID(stack);
    if (accountId == null) return -1;

    var accounts = player.getData(SmpDataAttachments.LOCAL_ACCOUNTS);
    if (!accounts.containsKey(accountId)) return -1;

    return accounts.get(accountId).money();
  }

  public static boolean hasAccessToAccount(UUID accountId, ServerPlayer player, Level level) {
    if (!(level instanceof ServerLevel serverLevel)) return false;

    WorldAccountEntry account = WorldAccountManager.getAccount(serverLevel, accountId);
    if (account == null) return false;

    return hasAccessToAccount(account, player);
  }

  public static boolean hasAccessToAccount(WorldAccountEntry account, ServerPlayer player) {
    return account.allowedAccess().contains(player.getUUID());
  }

  public static void setAccount(ItemStack stack, UUID accountId, ServerLevel level) {
    var account = WorldAccountManager.getAccount(level, accountId);
    if (account == null) return;

    stack.set(SmpComponents.OWNERSHIP, AccountData.create(accountId));
  }

  public static void clearAccount(ItemStack stack) {
    stack.remove(SmpComponents.OWNERSHIP);
  }

  public static boolean addMoney(UUID accountId, ServerPlayer player, int amount) {
    var level = (ServerLevel) player.level();

    WorldAccountEntry account = WorldAccountManager.getAccount(level, accountId);
    if (account == null || !hasAccessToAccount(account, player)) return false;

    if (account.money() + amount < 0) return false;

    WorldAccountEntry updatedAccount = account.withUpdatedBalance(account.money() + amount);
    WorldAccountManager.putAccount(level, accountId, updatedAccount);

    notifyPlayersWithAccess(level, accountId);
    return true;
  }

  public static boolean addMoney(UUID accountId, ServerLevel level, int amount) {
    WorldAccountEntry account = WorldAccountManager.getAccount(level, accountId);
    if (account == null) return false;

    if (account.money() + amount < 0) return false;

    WorldAccountEntry updatedAccount = account.withUpdatedBalance(account.money() + amount);
    WorldAccountManager.putAccount(level, accountId, updatedAccount);

    notifyPlayersWithAccess(level, accountId);
    return true;
  }

  public static boolean addMoney(WorldAccountEntry account, ServerPlayer player, int amount) {
    var level = (ServerLevel) player.level();
    var accountId = account.id();

    if (!hasAccessToAccount(account, player)) return false;

    if (account.money() + amount < 0) return false;

    WorldAccountEntry updatedAccount = account.withUpdatedBalance(account.money() + amount);
    WorldAccountManager.putAccount(level, accountId, updatedAccount);

    notifyPlayersWithAccess(level, accountId);
    return true;
  }

  public static void notifyPlayer(ServerLevel level, UUID playerId) {
    Map<UUID, WorldAccountEntry> accounts = WorldAccountManager.getAccounts(level);
    if (accounts.isEmpty()) return;

    // Find the target player on the server
    ServerPlayer targetPlayer = level.getServer().getPlayerList().getPlayer(playerId);
    if (targetPlayer == null) return;

    // Filter only accounts the player has access to
    var playerAccounts = accounts.entrySet().stream()
        .filter(e -> e.getValue().allowedAccess().contains(playerId))
        .map(e -> new LocalAccountEntry(
            e.getKey(), e.getValue().owner(), e.getValue().name(), e.getValue().money()))
        .toList();

    if (!playerAccounts.isEmpty()) {
      UpdatePlayerAccountsNet payload = new UpdatePlayerAccountsNet(playerAccounts);
      PacketDistributor.sendToPlayer(targetPlayer, payload);
    }
  }

  public static void notifyPlayersWithAccess(ServerLevel level, UUID accountId) {
    WorldAccountEntry account = WorldAccountManager.getAccount(level, accountId);
    if (account == null) return;

    for (UUID playerId : account.allowedAccess()) {
      notifyPlayer(level, playerId);
    }
  }
}
