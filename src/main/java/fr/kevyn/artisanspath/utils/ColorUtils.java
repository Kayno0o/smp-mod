package fr.kevyn.artisanspath.utils;

import java.util.List;

public class ColorUtils {
  private ColorUtils() {}

  public static int mergeHexColors(List<Integer> hexColors) {
    if (hexColors == null || hexColors.isEmpty()) return 0xFFFFFF;
    if (hexColors.size() == 1) return hexColors.get(0);

    int totalR = 0, totalG = 0, totalB = 0, maxComponent = 0;

    // extract RGB and find max component
    for (int color : hexColors) {
      int r = (color >> 16) & 0xFF;
      int g = (color >> 8) & 0xFF;
      int b = color & 0xFF;

      totalR += r;
      totalG += g;
      totalB += b;
      maxComponent = Math.max(maxComponent, Math.max(r, Math.max(g, b)));
    }

    // calculate averages
    int avgR = totalR / hexColors.size();
    int avgG = totalG / hexColors.size();
    int avgB = totalB / hexColors.size();
    int avgMax = maxComponent / hexColors.size();

    // apply brightness scaling (Minecraft's formula)
    float scale = (float) avgMax / Math.max(avgR, Math.max(avgG, avgB));

    int finalR = Math.min(255, Math.round(avgR * scale));
    int finalG = Math.min(255, Math.round(avgG * scale));
    int finalB = Math.min(255, Math.round(avgB * scale));

    return (finalR << 16) | (finalG << 8) | finalB;
  }

  public static int rgbToHex(int red, int green, int blue) {
    return (red << 16) | (green << 8) | blue;
  }

  public static int[] hexToRgb(int hex) {
    return new int[] {
      (hex >> 16) & 0xFF, // Red
      (hex >> 8) & 0xFF, // Green
      hex & 0xFF // Blue
    };
  }
}
