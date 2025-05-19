package fr.kevyn.smp.item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import fr.kevyn.smp.component.OwnershipData;
import fr.kevyn.smp.init.ModComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;

public class CardItem extends Item {
  public CardItem() {
    super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
  }

  @Override
  public void onCraftedBy(ItemStack stack, Level level, Player player) {
    super.onCraftedBy(stack, level, player);
    setOwner(stack, player);
  }

  @Nullable
  public Player getOwner(ItemStack stack, Level level) {
    if (stack.has(ModComponents.OWNERSHIP)) {
      OwnershipData ownerData = stack.get(ModComponents.OWNERSHIP);
      if (!ownerData.ownerUUID().isEmpty()) {
        return level.getPlayerByUUID(UUID.fromString(ownerData.ownerUUID()));
      }
    }
    return null;
  }

  public void setOwner(ItemStack stack, Player owner) {
    stack.set(ModComponents.OWNERSHIP, OwnershipData.create(owner.getStringUUID()));

    List<Component> loreList = new ArrayList<>();
    loreList.add(Component.literal("Owner: ").withStyle(ChatFormatting.GRAY)
        .append(Component.literal(owner.getName().getString()).withStyle(ChatFormatting.BLUE)));
    stack.set(DataComponents.LORE, new ItemLore(loreList));
  }

  public boolean isOwner(ItemStack stack, Player player, Level level) {
    Player owner = getOwner(stack, level);
    if (owner instanceof Player realPlayer)
      return realPlayer.getStringUUID().equals(player.getStringUUID());

    return false;
  }

  public void clearOwner(ItemStack stack) {
    stack.remove(ModComponents.OWNERSHIP);
    stack.remove(DataComponents.LORE);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
    if (player.isCrouching()) {
      ItemStack stack = player.getItemInHand(usedHand);
      if (this.isOwner(stack, player, level)) {
        this.clearOwner(stack);
        // level.playSound(player, player.getOnPos(), SoundEvents.BONE_BLOCK_BREAK,
        // SoundSource.BLOCKS, 1f, 1f);
        level.playSound(player, player.getOnPos(), SoundEvents.BONE_BLOCK_BREAK, SoundSource.BLOCKS, 1f, 0f);
      } else {
        this.setOwner(stack, player);
        level.playSound(player, player.getOnPos(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1f, 1f);
      }
    }
    return super.use(level, player, usedHand);
  }
}
