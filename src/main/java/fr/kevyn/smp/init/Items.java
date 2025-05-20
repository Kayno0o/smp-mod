package fr.kevyn.smp.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.item.ATMBlockItem;
import fr.kevyn.smp.item.CardItem;
import fr.kevyn.smp.item.MoneyItem;
import fr.kevyn.smp.item.RedstonePaygateBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Items {
  private static final Map<Integer, Item> VALUE_TO_MONEY = new HashMap<>();

  public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(SmpMod.MODID);
  public static final DeferredItem<Item> ATM = REGISTRY.register(ATMBlockItem.BLOCK.getId().getPath(),
      ATMBlockItem::new);
  public static final DeferredItem<Item> MONEY_1 = REGISTRY.register("money_1", () -> register(1));
  public static final DeferredItem<Item> MONEY_10 = REGISTRY.register("money_10", () -> register(10));
  public static final DeferredItem<Item> MONEY_100 = REGISTRY.register("money_100", () -> register(100));
  public static final DeferredItem<Item> MONEY_1000 = REGISTRY.register("money_1000", () -> register(1000));
  public static final DeferredItem<Item> CARD = REGISTRY.register("card", CardItem::new);
  public static final DeferredItem<Item> REDSTONE_PAYGATE = REGISTRY.register("redstone_paygate",
      RedstonePaygateBlockItem::new);

  private static Item register(int value) {
    Item item = new MoneyItem(value);
    VALUE_TO_MONEY.put(value, item);
    return item;
  }

  public static Optional<ItemStack> moneyStackFromValue(int value, int count) {
    Item item = VALUE_TO_MONEY.get(value);
    return item != null ? Optional.of(new ItemStack(item, count)) : Optional.empty();
  }
}
