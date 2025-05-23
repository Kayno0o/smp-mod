package fr.kevyn.smp.ui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public class SilentButton extends Button {
  public SilentButton(int x, int y, int width, int height, Component message, OnPress onPress) {
    super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
  }

  @Override
  public void playDownSound(SoundManager soundManager) {
    // do nothing to disable sound
  }
}
