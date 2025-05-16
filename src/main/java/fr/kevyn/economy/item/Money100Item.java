package fr.kevyn.economy.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class Money100Item extends Item {
	public Money100Item() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
	}
}
