package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class DynamicVelocityCommand extends CommandBase {
    RobotBase robotBase;
    Follower follower;
    Pose redGoalPose;
    Pose blueGoalPose;
    double velocity;
    public DynamicVelocityCommand(RobotBase m_robotBase, Follower m_follower, Pose m_redGoalPose, Pose m_blueGoalPose){
        robotBase = m_robotBase;
        follower = m_follower;
        redGoalPose = m_redGoalPose;
        blueGoalPose = m_blueGoalPose;
    }

    @Override
    public void initialize(){
        if(DataStorage.alliance == DecodeEnums.Alliance.BLUE){
            velocity = follower.getPose().distanceFrom(blueGoalPose);
            new SetAllLaunchVelocityCommandGroup(robotBase, velocity);
        }
        else{
            velocity = follower.getPose().distanceFrom(redGoalPose);
            new SetAllLaunchVelocityCommandGroup(robotBase, velocity);
        }
        super.initialize();
    }
}
