package net.fabricmc.example.item;

import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
  public static final FoodComponent FRUIT_APP = new FoodComponent.Builder().hunger(4)
  .saturationModifier(0.1f).build();
}