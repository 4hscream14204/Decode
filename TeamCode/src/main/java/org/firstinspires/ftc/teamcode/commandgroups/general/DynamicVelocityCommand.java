package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class DynamicVelocityCommand extends CommandBase {
    RobotBase robotBase;
    public DynamicVelocityCommand(RobotBase m_robotBase, Follower follower, Pose redGoalPose, Pose blueGoalPose){
        robotBase = m_robotBase;
    }

    @Override
    public void initialize(){

    }
}
