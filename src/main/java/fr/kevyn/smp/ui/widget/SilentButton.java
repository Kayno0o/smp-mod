package fr.kevyn.smp.ui.widget;

import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public class SilentButton extends AbstractButton {
  public SilentButton(int x, int y, int width, int height, Component message, OnPress onPress) {
    super(x, y, width, height, message, onPress);
  }

  @Override
  public void playDownSound(SoundManager soundManager) {
    // do nothing to disable sound
  }
}
