package fr.kevyn.smp.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {
  public static final NumberFormat CURRENCY_FORMAT;
  static {
    CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.FRANCE);
    CURRENCY_FORMAT.setMaximumFractionDigits(0);
    CURRENCY_FORMAT.setMinimumFractionDigits(0);
  }
}
