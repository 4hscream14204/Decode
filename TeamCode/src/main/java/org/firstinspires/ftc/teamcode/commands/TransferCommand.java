package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;

public class TransferCommand extends SequentialCommandGroup {
    RobotBase robotBase;
    Follower follower;
    public TransferCommand(RobotBase m_robotBase, Follower m_follower){
        robotBase = m_robotBase;
        follower = m_follower;
        addCommands(
            //new WaitUntilCommand(()->robotBase.launcherSubsystem.isAtSpeed()),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
            new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
            new WaitCommand(500),
            new InstantCommand(()->robotBase.intakeTransferSubsystem.stopAll()),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            new InstantCommand(()->robotBase.chassisSubsystem.setTargetHeading(37))
        );
    }
}
