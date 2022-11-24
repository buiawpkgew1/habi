package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.example.item.ModFoodComponents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HabiMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("habi");
    // 新物品的实例
    public static Item FRUIT_APP = new Item(new Item.Settings().group(ItemGroup.FOOD)
    .food(ModFoodComponents.FRUIT_APP));

	@Override
	public void onInitialize() {
        Registry.register(Registry.ITEM,new Identifier("habi","frur_pudding"),FRUIT_APP);
	}
}
