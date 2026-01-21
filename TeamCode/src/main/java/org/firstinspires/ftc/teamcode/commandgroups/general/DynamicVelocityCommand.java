package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;

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
    public void execute(){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            goalPose = new Pose(127.7, 131.7);
            //distance = robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose);
        }
        else{
            goalPose = new Pose(127.7, 131.7).mirror();
            //distance = robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose);
        }
        distance = follower.getPose().distanceFrom(goalPose) * 2.54;
        robotBase.launcherSubsystemLeft.setLaunchVelocity(distance);
        robotBase.launcherSubsystemMiddle.setLaunchVelocity(distance);
        robotBase.launcherSubsystemRight.setLaunchVelocity(distance);
    }

    @Override
    public boolean isFinished() {
        if (robotBase.tiltSubsystem.tiltPositionL == Tilt.Position.LEFTACTIVE) {
            new SetAllVelocityCommandGroup(robotBase, 0);
            return true;
        } else {
            return false;
        }
    }
}
