package fr.kevyn.smp.init;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.item.ATMBlockItem;
import fr.kevyn.smp.item.CardItem;
import fr.kevyn.smp.item.CoinItem;
import fr.kevyn.smp.item.MoneyItem;
import fr.kevyn.smp.item.RedstonePaygateBlockItem;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SmpItems {
  private static final Map<Integer, Item> VALUE_TO_MONEY = new HashMap<>();

  public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(SmpMod.MODID);
  public static final DeferredItem<Item> ATM =
      REGISTRY.register(ATMBlockItem.BLOCK.getId().getPath(), ATMBlockItem::new);
  public static final DeferredItem<Item> COIN =
      REGISTRY.register("coin", () -> registerMoneyItem(new CoinItem(), 1));
  public static final DeferredItem<Item> SMALL_BILL =
      REGISTRY.register("small_bill", () -> registerMoneyItem(10));
  public static final DeferredItem<Item> BIG_BILL =
      REGISTRY.register("big_bill", () -> registerMoneyItem(100));
  public static final DeferredItem<Item> MONEY_INGOT =
      REGISTRY.register("money_ingot", () -> registerMoneyItem(1000));
  public static final DeferredItem<Item> CARD = REGISTRY.register("card", CardItem::new);
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
}
