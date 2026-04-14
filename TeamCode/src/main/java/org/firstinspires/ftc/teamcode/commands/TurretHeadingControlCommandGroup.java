package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.base.RobotBase;

public class TurretHeadingControlCommandGroup extends CommandBase {
    RobotBase robotBase;
    Follower follower;
    double turretAngle;
    public TurretHeadingControlCommandGroup(RobotBase m_robotBase, Follower m_follower){
        robotBase = m_robotBase;
        follower = m_follower;
    }

    @Override
    public void execute(){
        turretAngle = robotBase.turretSubsystem.getTurretAngle(robotBase.chassisSubsystem.pinpoint, follower);
        robotBase.turretSubsystem.setPositionDeg(turretAngle);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
