package fr.kevyn.smp.data;

import java.util.Map;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class ServerAccountManager {
  private static final String DATA_NAME = "smp_world_accounts";

  private ServerAccountManager() {}

  public static WorldAccountSavedData getSavedData(ServerLevel level) {
    DimensionDataStorage storage = level.getDataStorage();
    return storage.computeIfAbsent(
        new SavedData.Factory<>(WorldAccountSavedData::create, WorldAccountSavedData::load),
        DATA_NAME);
  }

  public static WorldAccountSavedData getGlobalSavedData(MinecraftServer server) {
    ServerLevel overworld = server.overworld();
    return getSavedData(overworld);
  }

  public static Map<UUID, AccountEntry> getAccounts(ServerLevel level) {
    WorldAccountSavedData data = getSavedData(level);
    return data.getAccounts();
  }

  public static void putAccount(ServerLevel level, UUID accountId, AccountEntry account) {
    WorldAccountSavedData data = getSavedData(level);
    data.putAccount(accountId, account);
  }

  public static AccountEntry getAccount(ServerLevel level, UUID accountId) {
    WorldAccountSavedData data = getSavedData(level);
    return data.getAccount(accountId);
  }

  public static boolean hasAccount(ServerLevel level, UUID accountId) {
    WorldAccountSavedData data = getSavedData(level);
    return data.hasAccount(accountId);
  }

  public static AccountEntry removeAccount(ServerLevel level, UUID accountId) {
    WorldAccountSavedData data = getSavedData(level);
    return data.removeAccount(accountId);
  }

  public static boolean leaveAccount(ServerLevel level, UUID accountId, UUID playerId) {
    WorldAccountSavedData data = getSavedData(level);
    return data.removeAllowedPlayer(accountId, playerId);
  }
}
