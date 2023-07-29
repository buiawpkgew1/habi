package com.example.Item;

import com.example.ExampleMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Moditems {

    public static final Item REDS = regosterItem("reds",new Item(new FabricItemSettings()));
    public static final Item REDST = regosterItem("redst",new Item(new FabricItemSettings()));
    public static final Item REDSW = regosterItem("redsw",new Item(new FabricItemSettings()));
    private static Item regosterItem(String name,Item item){

        return Registry.register(Registries.ITEM,new Identifier(ExampleMod.MOD_ID,name),item);
    };

    public static void regosterModItem() {
        ExampleMod.LOGGER.info("为"+ExampleMod.MOD_ID+"注册模组物品");
    }
}
