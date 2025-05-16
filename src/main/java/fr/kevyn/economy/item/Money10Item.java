package fr.kevyn.economy.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class Money10Item extends Item {
	public Money10Item() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
	}
}
