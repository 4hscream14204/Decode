package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

public class FollowPath extends CommandBase {
    private final Follower follower;
    private final PathChain path;
    private boolean holdEnd = true;
    private double maxPower = 1;
    private double completionThreshold = 0.99;

    public FollowPath(Follower follower, PathChain pathChain) {
        this.follower = follower;
        this.path = pathChain;
    }

    public FollowPath(Follower follower, PathChain pathChain, double maxPower) {
        this.follower = follower;
        this.path = pathChain;
        this.maxPower = maxPower;
    }

    public FollowPath(Follower follower, PathChain pathChain, boolean holdEnd) {
        this.follower = follower;
        this.path = pathChain;
        this.holdEnd = holdEnd;
    }

    public FollowPath(Follower follower, PathChain pathChain, boolean holdEnd, double maxPower) {
        this.follower = follower;
        this.path = pathChain;
        this.holdEnd = holdEnd;
        this.maxPower = maxPower;
    }

    public FollowPath(Follower follower, Path path) {
        this(follower, new PathChain(path));
    }

    public FollowPath(Follower follower, Path path, double maxPower) {
        this(follower, new PathChain(path), maxPower);
    }

    /**
     * Decides whether or not to make the robot maintain its position once the path ends.
     *
     * @param holdEnd If the robot should maintain its ending position
     * @return This command for compatibility in command groups
     */
    public FollowPath setHoldEnd(boolean holdEnd) {
        this.holdEnd = holdEnd;
        return this;
    }

    /**
     * Sets the follower's maximum power
     * @param power Between 0 and 1
     * @return This command for compatibility in command groups
     */
    public FollowPath setMaxPower(double power) {
        this.maxPower = power;
        return this;
    }

    /**
     * Sets the T-value at which the follower will consider the path complete
     * @param t Between 0 and 1
     * @return This command for compatibility in command groups
     */
    public FollowPath setCompletionThreshold(double t) {
        this.completionThreshold = t;
        return this;
    }

    @Override
    public void initialize() {
        follower.setMaxPower(this.maxPower);
        follower.followPath(path, holdEnd);
    }

    @Override
    public boolean isFinished() {
        if ( follower.getCurrentPathNumber() == this.path.size() - 1 && Math.abs(follower.getHeadingError()) < 0.1 ) {
            return follower.getCurrentTValue() >= this.completionThreshold;
        }
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        follower.setMaxPower(1);
    }
}
