package net.github.buiawpkgew1;

import net.fabricmc.api.ModInitializer;
import net.github.buiawpkgew1.item.ModFoodComponents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HabiMod implements ModInitializer {
    public static final Logger VERSION = LoggerFactory.getLogger("1.0");
	public static final Logger LOGGER = LoggerFactory.getLogger("habi");
    // 新物品的实例
    public static Item FRUIT_APP = new Item(new Item.Settings().food(ModFoodComponents.FRUIT_APP));
    public static Item HABI_SED = new Item(new Item.Settings().food(ModFoodComponents.HABI_SED));
    public static ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier("example", "test_group"))
    .displayName(Text.literal("habit.group.1"))
    .icon(() -> new ItemStack(Items.DIAMOND))
    .entries((enabledFeatures, entries, operatorEnabled) -> {
        entries.add(Items.DIAMOND);
        entries.add(Items.CHORUS_FRUIT);
    })
    .build();
	@Override
	public void onInitialize() {
        Registry.register(Registries.ITEM,new Identifier("habit","app"),FRUIT_APP);
        Registry.register(Registries.ITEM,new Identifier("habit","sed"),HABI_SED);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(FRUIT_APP));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(FRUIT_APP));
	}
}
