package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Tilt;

public class DynamicVelocityCommand extends CommandBase {
    RobotBase robotBase;
    Follower follower;
    Pose goalPose;
    double distance;
    public DynamicVelocityCommand(RobotBase m_robotBase, Follower m_follower){
        robotBase = m_robotBase;
        follower = m_follower;
    }
    @Override
    public void initialize(){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            goalPose = new Pose(127.7, 131.7);
            //distance = robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose);
        }
        else{
            goalPose = new Pose(127.7, 131.7).mirror();
            //distance = robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose);
        }
        if (robotBase.tiltSubsystem.tiltPositionL == Tilt.Position.LEFTACTIVE) {
            robotBase.launcherSubsystemLeft.setVelocity(0);
            robotBase.launcherSubsystemMiddle.setVelocity(0);
            robotBase.launcherSubsystemRight.setVelocity(0);
        } else {
            distance = follower.getPose().distanceFrom(goalPose) * 2.54;
            robotBase.launcherSubsystemLeft.setLaunchVelocity(distance);
            robotBase.launcherSubsystemMiddle.setLaunchVelocity(distance);
            robotBase.launcherSubsystemRight.setLaunchVelocity(distance);
        }
    }
/*
    @Override
    public boolean isFinished() {
        if (robotBase.tiltSubsystem.tiltPositionL == Tilt.Position.LEFTACTIVE) {
            return false;
        } else {
            new SetAllVelocityCommandGroup(robotBase, 0);
            return true;
        }
    }*/
}
