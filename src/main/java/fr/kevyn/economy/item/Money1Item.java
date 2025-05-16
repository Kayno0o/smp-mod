package fr.kevyn.economy.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class Money1Item extends Item {
	public Money1Item() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
	}
}
