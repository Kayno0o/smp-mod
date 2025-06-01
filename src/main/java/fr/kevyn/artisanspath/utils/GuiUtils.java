package fr.kevyn.artisanspath.utils;

public class GuiUtils {
  public static int getCenterX(int containerLeft, int containerWidth) {
    return containerLeft + containerWidth / 2;
  }

  public static int getCenterX(int containerLeft, int containerWidth, int width) {
    return containerLeft + containerWidth / 2 - width / 2;
  }

  public static int getCenterY(int containerTop, int containerHeight) {
    return containerTop + containerHeight / 2;
  }

  public static int getCenterY(int containerTop, int containerHeight, int height) {
    return containerTop + containerHeight / 2 - height / 2;
  }
}
