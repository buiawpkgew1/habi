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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

public class HabiMod implements ModInitializer {
    public static final Logger VERSION = LoggerFactory.getLogger("1.0");
	public static final Logger LOGGER = LoggerFactory.getLogger("habi");
// 夜视的快捷键
    public static KeyBinding NIGHT_VISION_KEY_BINDING;

    // 夜视的状态变量
    public static boolean nightVisionEnabled = false;

    @Override
    public void onInitialize() {
        // 创建快捷键绑定
        NIGHT_VISION_KEY_BINDING = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("key.night_vision", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F, "category.my_mod")
        );

        // 注册事件监听器
    // 注册事件监听器
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if (nightVisionEnabled) {
                server.getPlayerManager().getPlayerList().forEach(player -> {
                    if (!player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 200, 0, true, false));
                    }
                });
            }
        });
    }

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
