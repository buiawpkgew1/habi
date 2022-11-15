package net.fabricmc.example.api;

import baritone.api.behavior.ILookBehavior;
import baritone.api.behavior.IPathingBehavior;
import baritone.api.cache.IWorldProvider;
import baritone.api.pathing.calc.IPathingControlManager;
import baritone.api.process.*;
import baritone.api.utils.IInputOverrideHandler;

public interface IBaritone {
    IPathingBehavior getPathingBehavior();
    ILookBehavior getLookBehavior();

    IFollowProcess getFollowProcess();
    IMineProcess getMineProcess();
    IBuilderProcess getBuilderProcess();
    IExploreProcess getExploreProcess();
    IFarmProcess getFarmProcess();
    ICustomGoalProcess getCustomGoalProcess();
    IGetToBlockProcess getGetToBlockProcess();
    IWorldProvider getWorldProvider();
    IPathingControlManager getPathingControlManager();
    IInputOverrideHandler getInputOverrideHandler();
    void openClick();
}