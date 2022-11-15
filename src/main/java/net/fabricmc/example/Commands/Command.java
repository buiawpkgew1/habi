package net.fabricmc.example.Commands;

import com.ibm.icu.impl.duration.impl.Utils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.datafixers.types.templates.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;

public class Command {
  protected static MinecraftClient mc;
  private final String name;
  private final String title;
  private final String description;
  private final List<String> aliases = new ArrayList<>();

  public Command(String name,String description,String... aliases){
      this.name=name;
      this.description=description;
      this.title= Utils.nameToTitle(name);
      Collections.addAll(this.aliases,aliases);
      mc = MinecraftClient.getInstance();
  }

    protected static <T> RequiredArgumentBuilder<CommandSource,T> argument(final String name,final ArgumentType<T> type){
      return RequiredArgumentBuilder.argument(name, type);
  }
    public String getName(){
      return name;
  }
    public List<String> getAliases() {
      return aliases;
  }


}