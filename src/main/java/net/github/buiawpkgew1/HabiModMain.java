package net.github.buiawpkgew1;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.github.buiawpkgew1.item.ModFoodComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HabiModMain implements ModInitializer {
    public static final Logger VERSION = LoggerFactory.getLogger("1.0");
    public static final Logger LOGGER = LoggerFactory.getLogger("habi");
    // 新物品的实例
    public static Item FRUIT_APP = new Item(new Item.Settings().food(ModFoodComponents.FRUIT_APP));
    public static Item HABI_SED = new Item(new Item.Settings().food(ModFoodComponents.HABI_SED));
    public static ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.literal("habit.group.1"))
            .icon(() -> new ItemStack(Items.DIAMOND))
            .entries((enabledFeatures, entries) -> {
                entries.add(Items.DIAMOND);
                entries.add(Items.CHORUS_FRUIT);
            })
            .build();
    private static KeyBinding flyKeyBinding;
    private static KeyBinding nightVisionKeyBinding;

    public static boolean isFlyKeyPressed() {
        return flyKeyBinding.isPressed();
    }

    public static boolean isNightVisionKeyPressed() {
        return nightVisionKeyBinding.isPressed();
    }

    private static boolean isOnClient() {
        return MinecraftClient.getInstance() != null;
    }

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier("habit", "app"), FRUIT_APP);
        Registry.register(Registries.ITEM, new Identifier("habit", "sed"), HABI_SED);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(FRUIT_APP));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(FRUIT_APP));

        flyKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.habi.fly", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F, "category.habi"
        ));
        nightVisionKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.habi.nightVision", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C, "category.habi"
        ));
        if (HabiModMain.isOnClient()) {
            ModKeyEvents.init();
        }
    }
}
