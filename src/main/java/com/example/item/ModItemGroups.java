package com.example.Item;

import com.example.ExampleMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final String GROUPS_TAB_NAME="itemgroup.runy";
    public static final ItemGroup BUBY_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(ExampleMod.MOD_ID,"ruby"),
            FabricItemGroup.builder().displayName(Text.translatable(GROUPS_TAB_NAME))
                    .icon(()->new ItemStack(Moditems.REDS))
                    .entries((displayContext, entries) -> {
                        entries.add(Moditems.REDS);
                        entries.add(Moditems.REDST);
                        entries.add(Moditems.REDSW);
                    }).build());

    public static void regosterModItemGroups() {
        ExampleMod.LOGGER.info("为 "+ExampleMod.MOD_ID+" 注册物品组");
    }
}
