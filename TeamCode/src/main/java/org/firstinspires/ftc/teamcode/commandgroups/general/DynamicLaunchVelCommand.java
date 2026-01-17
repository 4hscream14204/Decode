package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class DynamicLaunchVelCommand extends CommandBase {
    RobotBase robotBase;
    Follower follower;
    Pose goalPose;
    double distance;
    public DynamicLaunchVelCommand(RobotBase m_robotBase, Follower m_follower){
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
            distance = follower.getPose().distanceFrom(goalPose) * 2.54;
        robotBase.launcherSubsystemLeft.setLaunchVelocity(distance);
        robotBase.launcherSubsystemMiddle.setLaunchVelocity(distance);
        robotBase.launcherSubsystemRight.setLaunchVelocity(distance);
    }
}
