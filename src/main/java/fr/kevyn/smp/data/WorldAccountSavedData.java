package fr.kevyn.smp.data;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.component.WorldAccountEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

public class WorldAccountSavedData extends SavedData {
  private Map<UUID, WorldAccountEntry> accounts = new HashMap<>();

  // Create new instance of saved data
  public static WorldAccountSavedData create() {
    return new WorldAccountSavedData();
  }

  // Load existing instance of saved data
  public static WorldAccountSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
    WorldAccountSavedData data = WorldAccountSavedData.create();

    // Load the accounts map from NBT
    if (tag.contains("accounts", Tag.TAG_COMPOUND)) {
      CompoundTag accountsTag = tag.getCompound("accounts");
      for (String key : accountsTag.getAllKeys()) {
        try {
          UUID accountId = UUID.fromString(key);
          CompoundTag accountTag = accountsTag.getCompound(key);

          // Deserialize WorldAccountEntry from NBT
          WorldAccountEntry entry = WorldAccountEntry.fromNBT(accountTag);
          data.accounts.put(accountId, entry);
        } catch (IllegalArgumentException e) {
          // Skip invalid UUID keys
          SmpMod.LOGGER.warn("Invalid UUID key in saved data: {}", key);
        }
      }
    }

    return data;
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag accountsTag = new CompoundTag();

    for (Map.Entry<UUID, WorldAccountEntry> entry : accounts.entrySet()) {
      CompoundTag accountTag = entry.getValue().toNBT();
      accountsTag.put(entry.getKey().toString(), accountTag);
    }

    tag.put("accounts", accountsTag);
    return tag;
  }

  // Getter for accounts
  public Map<UUID, WorldAccountEntry> getAccounts() {
    return accounts;
  }

  // Add or update an account
  public void putAccount(UUID accountId, WorldAccountEntry account) {
    accounts.put(accountId, account);
    setDirty(); // Mark as dirty so it gets saved
  }

  // Remove an account
  public WorldAccountEntry removeAccount(UUID accountId) {
    WorldAccountEntry removed = accounts.remove(accountId);
    if (removed != null) {
      setDirty();
    }
    return removed;
  }

  // Check if account exists
  public boolean hasAccount(UUID accountId) {
    return accounts.containsKey(accountId);
  }

  // Get specific account
  public WorldAccountEntry getAccount(UUID accountId) {
    return accounts.get(accountId);
  }

  // Clear all accounts
  public void clearAccounts() {
    accounts.clear();
    setDirty();
  }
}
