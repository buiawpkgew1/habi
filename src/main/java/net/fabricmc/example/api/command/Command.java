package net.fabricmc.example.api.command;

import net.fabricmc.example.api.IBaritone;
import baritone.api.utils.IPlayerContext;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Command implements ICommand{
    protected IBaritone baritone;
    protected IPlayerContext ctx;

    protected final List<String> names;

    protected Command(IBaritone baritone, String... names){
        this.names= Collections.unmodifiableList(String.of(names)
        .map(s->s.toLowerCase(Locale.US))
        .collect(Collectors.toList()));
        this.baritone=baritone;
        this.ctx=baritone.getPlayerContext();
    }
    @Override
    public final List<String> getNames(){
        return this.names;
    }
}