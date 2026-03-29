package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.controller.PIDFController;

import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.LiftDistanceSensor;

public class LiftControlCommand extends CommandBase {
    RobotBase robotBase;
    double distanceFromTarget;
    double pidOutput;
    double pidOutputToServoPosition;
    public LiftControlCommand(RobotBase m_robotBase){
        robotBase = m_robotBase;
    }

    @Override
    public void execute() {
        distanceFromTarget = LiftDistanceSensor.LiftPosition.PARKMM.position - robotBase.liftDistanceSensorSubsystem.getPosition();
        pidOutput = robotBase.liftSubsystem.liftPIDF.calculate(distanceFromTarget);
        pidOutputToServoPosition = ((pidOutput + 1) / 2);
        robotBase.liftSubsystem.setPosition(pidOutputToServoPosition);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
