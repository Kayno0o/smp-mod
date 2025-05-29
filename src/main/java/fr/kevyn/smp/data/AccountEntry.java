package fr.kevyn.smp.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.kevyn.smp.component.CustomCodec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public record AccountEntry(
    UUID id, UUID owner, String name, int money, Map<UUID, String> allowedAccess) {
  public AccountEntry withUpdatedBalance(int newMoney) {
    return new AccountEntry(this.id, this.owner, this.name, newMoney, this.allowedAccess);
  }

  public static final Codec<AccountEntry> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
          CustomCodec.UUID.fieldOf("id").forGetter(AccountEntry::id),
          CustomCodec.UUID.fieldOf("owner").forGetter(AccountEntry::owner),
          Codec.STRING.fieldOf("name").forGetter(AccountEntry::name),
          Codec.INT.fieldOf("money").forGetter(AccountEntry::money),
          Codec.unboundedMap(CustomCodec.UUID, Codec.STRING)
              .fieldOf("allowedAccess")
              .forGetter(AccountEntry::allowedAccess))
      .apply(instance, AccountEntry::new));

  public CompoundTag toNBT() {
    CompoundTag tag = new CompoundTag();
    tag.putUUID("id", this.id());
    tag.putUUID("owner", this.owner());
    tag.putString("name", this.name());
    tag.putInt("money", this.money());

    ListTag accessList = new ListTag();
    for (Map.Entry<UUID, String> entry : this.allowedAccess().entrySet()) {
      CompoundTag playerTag = new CompoundTag();
      playerTag.putUUID("playerId", entry.getKey());
      playerTag.putString("playerName", entry.getValue());
      accessList.add(playerTag);
    }
    tag.put("allowedAccess", accessList);

    return tag;
  }

  public static AccountEntry fromNBT(CompoundTag tag) {
    UUID id = tag.getUUID("id");
    UUID owner = tag.getUUID("owner");
    String name = tag.getString("name");
    int money = tag.getInt("money");

    Map<UUID, String> allowedAccess = new HashMap<>();
    if (tag.contains("allowedAccess", Tag.TAG_LIST)) {
      ListTag accessList = tag.getList("allowedAccess", Tag.TAG_COMPOUND);
      for (int i = 0; i < accessList.size(); i++) {
        CompoundTag playerTag = accessList.getCompound(i);
        UUID playerId = playerTag.getUUID("playerId");
        String playerName = playerTag.getString("playerName");
        allowedAccess.put(playerId, playerName);
      }
    }

    return new AccountEntry(id, owner, name, money, allowedAccess);
  }
}
