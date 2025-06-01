package fr.kevyn.artisanspath.init;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.item.ATMBlockItem;
import fr.kevyn.artisanspath.item.CoinItem;
import fr.kevyn.artisanspath.item.MoneyItem;
import fr.kevyn.artisanspath.item.PaymentCardItem;
import fr.kevyn.artisanspath.item.RedstonePaygateBlockItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArtisansItems {
  private ArtisansItems() {}

  private static final Map<Integer, Item> VALUE_TO_MONEY = new HashMap<>();

  public static final DeferredRegister.Items REGISTRY =
      DeferredRegister.createItems(ArtisansMod.MODID);
  public static final DeferredItem<Item> ATM = REGISTRY.register("atm", ATMBlockItem::new);
  public static final DeferredItem<Item> COIN =
      REGISTRY.register("coin", () -> registerMoneyItem(new CoinItem(), 1));
  public static final DeferredItem<Item> PILE_OF_COINS =
      REGISTRY.register("pile_of_coins", () -> registerMoneyItem(10));
  public static final DeferredItem<Item> LARGE_PILE_OF_COINS =
      REGISTRY.register("large_pile_of_coins", () -> registerMoneyItem(100));
  public static final DeferredItem<Item> COIN_INGOT =
      REGISTRY.register("coin_ingot", () -> registerMoneyItem(1000));
  public static final DeferredItem<Item> PAYMENT_CARD =
      REGISTRY.register("payment_card", PaymentCardItem::new);
  public static final DeferredItem<Item> REDSTONE_PAYGATE =
      REGISTRY.register("redstone_paygate", RedstonePaygateBlockItem::new);

  private static Item registerMoneyItem(int value) {
    Item item = new MoneyItem(value);
    VALUE_TO_MONEY.put(value, item);
    return item;
  }

  private static Item registerMoneyItem(Item item, int value) {
    VALUE_TO_MONEY.put(value, item);
    return item;
  }

  public static Optional<ItemStack> moneyStackFromValue(int value, int count) {
    Item item = VALUE_TO_MONEY.get(value);
    return item != null ? Optional.of(new ItemStack(item, count)) : Optional.empty();
  }

  public static List<ItemStack> createChangeFromAmount(int amount) {
    List<ItemStack> stacks = new ArrayList<>();
    int remaining = amount;

    List<Integer> values =
        VALUE_TO_MONEY.keySet().stream().sorted(Collections.reverseOrder()).toList();

    for (int value : values) {
      if (remaining >= value) {
        int count = remaining / value;
        Item item = VALUE_TO_MONEY.get(value);

        while (count > 0) {
          int stackCount = Math.min(count, item.getMaxStackSize(new ItemStack(item)));
          stacks.add(new ItemStack(item, stackCount));
          count -= stackCount;
        }

        remaining %= value;
      }
    }

    return stacks;
  }
}
