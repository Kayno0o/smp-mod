package fr.kevyn.smp.utils;

import fr.kevyn.smp.component.AccountData;
import fr.kevyn.smp.data.AccountEntry;
import fr.kevyn.smp.data.ServerAccountManager;
import fr.kevyn.smp.init.SmpComponents;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.network.client.UpdatePlayerAccountsPacket;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class AccountUtils {
  public static final int MAX_ACCOUNT = 6;
  public static final int MAX_ALLOWED_ACCESS_COUNT = 3;

  private AccountUtils() {}

  @Nullable public static UUID getAccountUUID(ItemStack stack) {
    if (stack.has(SmpComponents.OWNERSHIP)) {
      AccountData accountData = stack.get(SmpComponents.OWNERSHIP);
      if (accountData.hasAccount()) return accountData.accountUUID();
    }
    return null;
  }

  @OnlyIn(Dist.CLIENT)
  @Nullable public static AccountEntry getLocalAccount(ItemStack stack, LocalPlayer player) {
    if (!player.hasData(SmpDataAttachments.ACCOUNTS)) return null;

    var accountId = getAccountUUID(stack);
    if (accountId == null) return null;

    var accounts = player.getData(SmpDataAttachments.ACCOUNTS);
    if (!accounts.containsKey(accountId)) return null;

    return accounts.get(accountId);
  }

  @Nullable public static AccountEntry getAccount(Level level, UUID accountId) {
    if (!(level instanceof ServerLevel serverLevel)) return null;
    return ServerAccountManager.getAccount(serverLevel, accountId);
  }

  @Nullable public static AccountEntry getAccount(Level level, ItemStack stack) {
    if (!(level instanceof ServerLevel serverLevel)) return null;

    var accountId = getAccountUUID(stack);
    if (accountId == null) return null;

    return ServerAccountManager.getAccount(serverLevel, accountId);
  }

  public static boolean hasAccessToAccount(ItemStack stack, ServerPlayer player, Level level) {
    UUID accountId = getAccountUUID(stack);
    if (accountId == null) return false;

    return hasAccessToAccount(accountId, player, level);
  }

  public static int getMoney(ItemStack stack, LocalPlayer player) {
    if (!player.hasData(SmpDataAttachments.ACCOUNTS)) return -1;

    var accountId = getAccountUUID(stack);
    if (accountId == null) return -1;

    var accounts = player.getData(SmpDataAttachments.ACCOUNTS);
    if (!accounts.containsKey(accountId)) return -1;

    return accounts.get(accountId).money();
  }

  public static boolean hasAccessToAccount(UUID accountId, ServerPlayer player, Level level) {
    if (!(level instanceof ServerLevel serverLevel)) return false;

    AccountEntry account = ServerAccountManager.getAccount(serverLevel, accountId);
    if (account == null) return false;

    return hasAccessToAccount(account, player);
  }

  public static boolean hasAccessToAccount(AccountEntry account, ServerPlayer player) {
    if (account.owner().equals(player.getUUID())) return true;

    return account.allowedAccess().containsKey(player.getUUID());
  }

  public static void setAccount(ItemStack stack, UUID accountId, ServerLevel level) {
    var account = ServerAccountManager.getAccount(level, accountId);
    if (account == null) return;

    stack.set(SmpComponents.OWNERSHIP, AccountData.create(accountId));
  }

  public static boolean addMoneyWithAuthorization(UUID accountId, ServerPlayer player, int amount) {
    var level = (ServerLevel) player.level();

    AccountEntry account = ServerAccountManager.getAccount(level, accountId);
    if (account == null || !hasAccessToAccount(account, player)) return false;

    if (account.money() + amount < 0) return false;

    AccountEntry updatedAccount = account.withUpdatedBalance(account.money() + amount);
    ServerAccountManager.putAccount(level, accountId, updatedAccount);

    notifyPlayersWithAccess(level, updatedAccount);
    return true;
  }

  public static boolean addMoneyWithAuthorization(
      AccountEntry account, ServerPlayer player, int amount) {
    var level = (ServerLevel) player.level();
    var accountId = account.id();

    if (!hasAccessToAccount(account, player)) return false;

    if (account.money() + amount < 0) return false;

    AccountEntry updatedAccount = account.withUpdatedBalance(account.money() + amount);
    ServerAccountManager.putAccount(level, accountId, updatedAccount);

    notifyPlayersWithAccess(level, updatedAccount);
    return true;
  }

  public static boolean addMoney(UUID accountId, ServerLevel level, int amount) {
    AccountEntry account = ServerAccountManager.getAccount(level, accountId);
    if (account == null) return false;

    if (account.money() + amount < 0) return false;

    AccountEntry updatedAccount = account.withUpdatedBalance(account.money() + amount);
    ServerAccountManager.putAccount(level, accountId, updatedAccount);

    notifyPlayersWithAccess(level, updatedAccount);
    return true;
  }

  public static List<AccountEntry> getPlayerAccounts(
      Map<UUID, AccountEntry> accounts, UUID playerId) {
    return accounts.values().stream()
        .filter(e -> e.allowedAccess().containsKey(playerId))
        .toList();
  }

  public static void notifyPlayer(ServerLevel level, UUID playerId) {
    Map<UUID, AccountEntry> accounts = ServerAccountManager.getAccounts(level);
    if (accounts.isEmpty()) return;

    // Find the target player on the server
    ServerPlayer targetPlayer = level.getServer().getPlayerList().getPlayer(playerId);
    if (targetPlayer == null) return;

    // Filter only accounts the player has access to
    var playerAccounts = getPlayerAccounts(accounts, playerId);

    if (!playerAccounts.isEmpty()) {
      UpdatePlayerAccountsPacket payload = new UpdatePlayerAccountsPacket(playerAccounts);
      PacketDistributor.sendToPlayer(targetPlayer, payload);
    }
  }

  public static void notifyPlayersWithAccess(ServerLevel level, UUID accountId) {
    AccountEntry account = ServerAccountManager.getAccount(level, accountId);
    if (account == null) return;

    notifyPlayersWithAccess(level, account);
  }

  public static void notifyPlayersWithAccess(ServerLevel level, AccountEntry account) {
    for (var playerId : account.allowedAccess().entrySet()) {
      notifyPlayer(level, playerId.getKey());
    }
  }

  public static boolean accountsChanged(
      Map<UUID, AccountEntry> oldMap, List<AccountEntry> newAccounts) {
    if (oldMap.size() != newAccounts.size()) return true;

    for (AccountEntry newAccount : newAccounts) {
      AccountEntry oldAccount = oldMap.get(newAccount.id());
      if (oldAccount == null
          || oldAccount.money() != newAccount.money()
          || !Objects.equals(oldAccount.name(), newAccount.name())
          || !Objects.equals(oldAccount.allowedAccess(), newAccount.allowedAccess())) {
        return true;
      }
    }
    return false;
  }
}
