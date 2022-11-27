package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.example.item.ModFoodComponents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HabiMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("habi");
    // 新物品的实例
    public static Item FRUIT_APP = new Item(new Item.Settings().food(ModFoodComponents.FRUIT_APP));
    public static Item HABI_SED = new Item(new Item.Settings().food(ModFoodComponents.HABI_SED));
    public static ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier("example", "test_group"))
    .displayName(Text.literal("Example Item Group"))
    .icon(() -> new ItemStack(Items.DIAMOND))
    .entries((enabledFeatures, entries, operatorEnabled) -> {
        entries.add(Items.DIAMOND);
    })
    .build();
	@Override
	public void onInitialize() {
        Registry.register(Registries.ITEM,new Identifier("habi","app"),FRUIT_APP);
        Registry.register(Registries.ITEM,new Identifier("habi","sed"),HABI_SED);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(FRUIT_APP));
	}
}
