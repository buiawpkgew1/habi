package net.fabricmc.example.api.command;

import baritone.api.command.argparser.IArgParserManager;

public interface ICommandSystem {
    IArgParserManager getParserManager();
}