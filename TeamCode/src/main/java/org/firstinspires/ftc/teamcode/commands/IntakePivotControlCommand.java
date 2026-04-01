package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;

public class IntakePivotControlCommand extends CommandBase {
    RobotBase robotBase;
    IntakePivot.PivotPosition pivotPosition;
    public IntakePivotControlCommand(RobotBase m_robotBase, IntakePivot.PivotPosition m_pivotPosition){
        robotBase = m_robotBase;
        pivotPosition = m_pivotPosition;
    }

    @Override
    public void execute(){
        robotBase.intakePivotSubsystem.setPosition(pivotPosition.value);
    }
}
