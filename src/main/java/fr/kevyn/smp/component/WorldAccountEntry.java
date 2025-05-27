package fr.kevyn.smp.component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public record WorldAccountEntry(UUID id, String name, int money, List<UUID> allowedAccess) {
  public WorldAccountEntry withUpdatedBalance(int newMoney) {
    return new WorldAccountEntry(this.id, this.name, newMoney, this.allowedAccess);
  }

  public CompoundTag toNBT() {
    CompoundTag tag = new CompoundTag();
    tag.putUUID("id", this.id());
    tag.putString("name", this.name());
    tag.putInt("money", this.money());

    ListTag accessList = new ListTag();
    for (UUID playerId : this.allowedAccess()) {
      CompoundTag playerTag = new CompoundTag();
      playerTag.putUUID("playerId", playerId);
      accessList.add(playerTag);
    }
    tag.put("allowedAccess", accessList);

    return tag;
  }

  public static WorldAccountEntry fromNBT(CompoundTag tag) {
    UUID id = tag.getUUID("id");
    String name = tag.getString("name");
    int money = tag.getInt("money");

    List<UUID> allowedAccess = new ArrayList<>();
    if (tag.contains("allowedAccess", Tag.TAG_LIST)) {
      ListTag accessList = tag.getList("allowedAccess", Tag.TAG_COMPOUND);
      for (int i = 0; i < accessList.size(); i++) {
        CompoundTag playerTag = accessList.getCompound(i);
        allowedAccess.add(playerTag.getUUID("playerId"));
      }
    }

    return new WorldAccountEntry(id, name, money, allowedAccess);
  }
}
