package net.fabricmc.example.api;

import baritone.api.behavior.IBehavior;
import baritone.api.pathing.calc.IPath;
import baritone.api.pathing.calc.IPathFinder;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.path.IPathExecutor;

import java.util.Optional;

public interface IPathingBehavior extends IBehavior {
    default Optional<Double> ticksRemainingInSegment() {
        return ticksRemainingInSegment(true);
        }

    default Optional<Double> ticksRemainingInSegment(boolean includeCurrentMovement) {
                IPathExecutor current = getCurrent();
                if (current == null) {
                        return Optional.empty();
                    }
                int start = includeCurrentMovement ? current.getPosition() : current.getPosition() + 1;
                return Optional.of(current.getPath().ticksRemainingFrom(start));
            }

    /**
062     * Returns the estimated remaining ticks to the current goal.
063     * Given that the return type is an optional, {@link Optional#empty()}
064     * will be returned in the case that there is no current goal.
065     *
066     * @return The estimated remaining ticks to the current goal.
067     */
    Optional<Double> estimatedTicksToGoal();

    /**
071     * @return The current pathing goal
072     */
    Goal getGoal();

    /**
076     * @return Whether or not a path is currently being executed. This will be false if there's currently a pause.
077     * @see #hasPath()
078     */
    boolean isPathing();

    /**
082     * @return If there is a current path. Note that the path is not necessarily being executed, for example when there
083     * is a pause in effect.
084     * @see #isPathing()
085     */
    default boolean hasPath() {
                return getCurrent() != null;
            }

    /**
091     * Cancels the pathing behavior or the current path calculation, and all processes that could be controlling path.
092     * <p>
093     * Basically, "MAKE IT STOP".
094     *
095     * @return Whether or not the pathing behavior was canceled. All processes are guaranteed to be canceled, but the
096     * PathingBehavior might be in the middle of an uncancelable action like a parkour jump
097     */
    boolean cancelEverything();

    /**
     * PLEASE never call this
     * <p>
     * If cancelEverything was like "kill" this is "sudo kill -9". Or shutting off your computer.
     */
    void forceCancel();

    /**
     * Returns the current path, from the current path executor, if there is one.
     *
     * @return The current path
     */
    default Optional<IPath> getPath() {
                return Optional.ofNullable(getCurrent()).map(IPathExecutor::getPath);
            }

    /**
     * @return The current path finder being executed
     */
    Optional<? extends IPathFinder> getInProgress();

    /**
     * @return The current path executor
     */
    IPathExecutor getCurrent();

    /**
     * Returns the next path executor, created when planning ahead.
     *
     * @return The next path executor
     */
    IPathExecutor getNext();
}