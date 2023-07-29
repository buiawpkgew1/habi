package com.example;

import com.example.Item.ModItemGroups;
import com.example.Item.Moditems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
	public static final String MOD_ID= "templatemod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Moditems.regosterModItem();
		ModItemGroups.regosterModItemGroups();
		LOGGER.info("Hello Fabric world!");
	}
}