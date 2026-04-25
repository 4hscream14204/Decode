package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.base.RobotBase;

public class TurretHeadingControlManualCommand extends CommandBase {
    RobotBase robotBase;
    Follower follower;
    double turretAngle;
    boolean isUsingManualControl;
    double manualControlPosition;
    public TurretHeadingControlManualCommand(RobotBase m_robotBase, Follower m_follower, boolean manualControl, double manualPosition){
        robotBase = m_robotBase;
        follower = m_follower;
        isUsingManualControl = manualControl;
        manualControlPosition = manualPosition;
    }

    @Override
    public void execute(){
        if(isUsingManualControl == false){
            turretAngle = robotBase.turretSubsystem.getTurretAngle(robotBase.chassisSubsystem.pinpoint, follower);
            robotBase.turretSubsystem.updatePosition(turretAngle);
        }
        else{
            robotBase.turretSubsystem.setPosition(manualControlPosition);
        }
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}