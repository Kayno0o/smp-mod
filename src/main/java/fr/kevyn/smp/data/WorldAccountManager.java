package fr.kevyn.smp.data;

import fr.kevyn.smp.component.WorldAccountEntry;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class WorldAccountManager {
  private static final String DATA_NAME = "smp_world_accounts";

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

  public static Map<UUID, WorldAccountEntry> getAccounts(ServerLevel level) {
    WorldAccountSavedData data = getSavedData(level);
    return data.getAccounts();
  }

  public static void putAccount(ServerLevel level, UUID accountId, WorldAccountEntry account) {
    WorldAccountSavedData data = getSavedData(level);
    data.putAccount(accountId, account);
  }

  public static WorldAccountEntry getAccount(ServerLevel level, UUID accountId) {
    WorldAccountSavedData data = getSavedData(level);
    return data.getAccount(accountId);
  }

  public static boolean hasAccount(ServerLevel level, UUID accountId) {
    WorldAccountSavedData data = getSavedData(level);
    return data.hasAccount(accountId);
  }

  public static WorldAccountEntry removeAccount(ServerLevel level, UUID accountId) {
    WorldAccountSavedData data = getSavedData(level);
    return data.removeAccount(accountId);
  }
}
