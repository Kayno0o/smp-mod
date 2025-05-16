package fr.kevyn.economy.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CardItem extends Item {
	public CardItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
	}
}
