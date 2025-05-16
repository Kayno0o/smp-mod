package fr.kevyn.economy.init;

import fr.kevyn.economy.EconomyMod;
import fr.kevyn.economy.item.Money1Item;
import fr.kevyn.economy.item.Money10Item;
import fr.kevyn.economy.item.Money100Item;
import fr.kevyn.economy.item.ATMBlockItem;
import fr.kevyn.economy.item.CardItem;
import fr.kevyn.economy.item.Money1000Item;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Items {
  public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(EconomyMod.MODID);
  public static final DeferredItem<Item> ATM = REGISTRY.register(ATMBlockItem.BLOCK.getId().getPath(),
      ATMBlockItem::new);
  public static final DeferredItem<Item> MONEY_1 = REGISTRY.register("money_1", Money1Item::new);
  public static final DeferredItem<Item> MONEY_10 = REGISTRY.register("money_10", Money10Item::new);
  public static final DeferredItem<Item> MONEY_100 = REGISTRY.register("money_100", Money100Item::new);
  public static final DeferredItem<Item> MONEY_1000 = REGISTRY.register("money_1000", Money1000Item::new);
  public static final DeferredItem<Item> CARD = REGISTRY.register("card", CardItem::new);
}
