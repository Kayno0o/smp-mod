package fr.kevyn.smp.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class SoundUtils {
  private SoundUtils() {}

  public static final SoundEvent DISABLED = SoundEvents.VILLAGER_NO;
  public static final SoundEvent DELETE = SoundEvents.GLASS_BREAK;
  public static final SoundEvent SUCCESS = SoundEvents.EXPERIENCE_ORB_PICKUP;

  public static void notify(ServerPlayer player, SoundEvent sound) {
    player.level().playSound(player, player.getOnPos(), sound, SoundSource.BLOCKS, 1f, 0f);
  }
}
