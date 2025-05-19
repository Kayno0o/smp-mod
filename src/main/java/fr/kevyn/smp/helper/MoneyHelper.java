package fr.kevyn.smp.helper;

import java.text.NumberFormat;
import java.util.Locale;

public class MoneyHelper {
  public static final NumberFormat CURRENCY_FORMAT;
  static {
    CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.FRANCE);
    CURRENCY_FORMAT.setMaximumFractionDigits(0);
    CURRENCY_FORMAT.setMinimumFractionDigits(0);
  }

  public static String formatCompact(long value) {
    if (value < 1_000)
      return String.valueOf(value) + "€";
    if (value < 1_000_000)
      return value / 1_000 + "k" + (value % 1_000) / 10 + "€";
    if (value < 1_000_000_000)
      return value / 1_000_000 + "m" + (value % 1_000_000) / 10_000 + "€";
    return value / 1_000_000_000 + "M" + (value % 1_000_000_000) / 10_000_000 + "€";
  }
}
