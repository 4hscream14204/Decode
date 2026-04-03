package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.base.RobotBase;

public class TransferCommand extends CommandBase {
    RobotBase robotBase;
    public TransferCommand(RobotBase m_robotBase){
        robotBase = m_robotBase;
    }

    @Override
    public void execute(){
        robotBase.intakeTransferSubsystem.intakeAndTransfer();
        CommandScheduler.getInstance().schedule(new WaitCommand(500));
        robotBase.intakeTransferSubsystem.stopAll();
    }
}
