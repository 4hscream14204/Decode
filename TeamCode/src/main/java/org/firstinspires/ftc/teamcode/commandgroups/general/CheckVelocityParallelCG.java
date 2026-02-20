package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class CheckVelocityParallelCG extends ParallelCommandGroup {
    RobotBase robotBase;
    Pose goalPose;
    Follower follower;
    public CheckVelocityParallelCG(RobotBase m_robotBase, Follower m_follower) {
        robotBase = m_robotBase;
        follower = m_follower;
        if (DataStorage.alliance == DecodeEnums.Alliance.RED) {
            goalPose = new Pose(127.7, 131.7);
        } else {
            goalPose = new Pose(127.7, 131.7).mirror();
        }
        addCommands(
                new WaitUntilCommand(() -> robotBase.launcherSubsystemLeft.isAtSpeed(
                        robotBase.launcherSubsystemLeft.getLaunchVelocity(
                                robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose)))),
                new WaitUntilCommand(() -> robotBase.launcherSubsystemMiddle.isAtSpeed(
                        robotBase.launcherSubsystemMiddle.getLaunchVelocity(
                                robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose)))),
                new WaitUntilCommand(() -> robotBase.launcherSubsystemRight.isAtSpeed(
                        robotBase.launcherSubsystemRight.getLaunchVelocity(
                                robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose)))));
    }
}
