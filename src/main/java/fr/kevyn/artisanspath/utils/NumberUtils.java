package fr.kevyn.artisanspath.utils;

import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.client.Minecraft;

public class NumberUtils {
  private NumberUtils() {}

  public static NumberFormat getCurrencyFormat() {
    Locale locale = Minecraft.getInstance().getLocale();
    NumberFormat format = NumberFormat.getCurrencyInstance(locale);
    format.setMaximumFractionDigits(0);
    format.setMinimumFractionDigits(0);
    return format;
  }

  // Keep the static one for compatibility
  public static final NumberFormat CURRENCY_FORMAT;

  static {
    CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.FRANCE);
    CURRENCY_FORMAT.setMaximumFractionDigits(0);
    CURRENCY_FORMAT.setMinimumFractionDigits(0);
  }
}
