package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.Robot;

import org.firstinspires.ftc.teamcode.base.RobotBase;

public class AutoTurretHeadingCommand extends CommandBase {
    RobotBase robotBase;
    Follower follower;
    Pose goalPose;
    public AutoTurretHeadingCommand(RobotBase m_robotBase, Follower m_follower, Pose m_goalPose){
        robotBase = m_robotBase;
        follower = m_follower;
        goalPose = m_goalPose;
    }

    @Override
    public void execute(){
        robotBase.turretSubsystem.updatePosition(robotBase.turretSubsystem.getTurretAngle(robotBase.chassisSubsystem.pinpoint, follower, goalPose));
    }
}
