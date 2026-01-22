package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class Launch3ArtifactsDynamicCG extends SequentialCommandGroup {
    RobotBase robotBase;
    Pose goalPose;
    Follower follower;
    public Launch3ArtifactsDynamicCG(RobotBase m_robotBase, Follower m_follower){
        robotBase = m_robotBase;
        follower = m_follower;
        if(DataStorage.alliance == DecodeEnums.Alliance.RED) {
            goalPose = new Pose(127.7, 131.7);
        }
        else{
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
                                robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose)))),
                new Transfer3BallsNoCameraCommandGroup(robotBase));
    }
    }
