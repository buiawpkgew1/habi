package net.fabricmc.example.systems.commands.commands;

import baritone.api.BaritoneAPI;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.command.CommandSource;
import net.fabricmc.example.Commands.Command;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class BaritoneCommand {
    public BaritoneCommand(){
      super();
    }
    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder){
        builder.then(argument("command", StringArgumentType.greedyString())
        .executes(context->{
            String command=context.getArgument("command",String.class);
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(command);
            return SINGLE_SUCCESS;
        }));
    }



}