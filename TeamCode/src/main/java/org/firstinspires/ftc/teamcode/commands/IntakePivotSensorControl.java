package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.IntakeTransfer;

public class IntakePivotSensorControl extends CommandBase {
    RobotBase robotBase;
    public double leftXBallMin = 12.5;
    public double leftXBallMax = 13.3;
    public double rightXBallMin = 16.5;
    public double rightXBallMax = 19;
    public double wBallMin = 2;
    public double wBallMax = 4;
    public IntakePivotSensorControl(RobotBase m_robotBase){
        robotBase = m_robotBase;
    }

    @Override
    public void execute(){
        if(robotBase.intakeLIntakeDistanceSensorSubsystem.getDistance() <= wBallMax && robotBase.intakeLIntakeDistanceSensorSubsystem.getDistance() >= wBallMin){
            robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK);
        }
        else{
            robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE);
        }
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
