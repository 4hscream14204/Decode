package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
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
    boolean isLockingToGate;
    public Launch3ArtifactsDynamicCG(RobotBase m_robotBase, Follower m_follower, boolean m_isLockingToGate){
        robotBase = m_robotBase;
        follower = m_follower;
        isLockingToGate = m_isLockingToGate;
        if(DataStorage.alliance == DecodeEnums.Alliance.RED) {
            goalPose = new Pose(127.7, 131.7);
        }
        else{
            goalPose = new Pose(127.7, 131.7).mirror();
        }
        addCommands(
                new CheckVelocityParallelCG(robotBase, follower),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                new InstantCommand(()->robotBase.chassisSubsystem.bolSnapToTarget = false),
                new ConditionalCommand(new AlignToGateHeadingLock(robotBase), new InstantCommand(()->robotBase.chassisSubsystem.isLockingToGate = false), ()->robotBase.chassisSubsystem.isLockingToGate));
    }
    }
