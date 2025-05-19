package fr.kevyn.smp.custom;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public class SilentButton extends Button {
  public SilentButton(Component message, int x, int y, int width, int height, OnPress onPress) {
    super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
  }

  @Override
  public void playDownSound(SoundManager soundManager) {
    // do nothing to disable sound
  }
}