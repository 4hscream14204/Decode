package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.Prism;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;

public class TransferCommand extends SequentialCommandGroup {
    RobotBase robotBase;
    Follower follower;
    public TransferCommand(RobotBase m_robotBase, Follower m_follower){
        robotBase = m_robotBase;
        follower = m_follower;
        if(robotBase.chassisSubsystem.isInCloseZone()){
            addCommands(
                    //new WaitUntilCommand(()->robotBase.launcherSubsystem.isAtSpeed()),
                    new InstantCommand(()->robotBase.prismSubsystem.setPosition(Prism.PrismModes.LAUNCH)),
                    new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                    new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
                    new WaitCommand(500),
                    new InstantCommand(()->robotBase.intakeTransferSubsystem.stopAll()),
                    new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                    new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                    new InstantCommand(()->robotBase.chassisSubsystem.setTargetHeading(37)),
                    new InstantCommand(()->robotBase.prismSubsystem.setPosition(Prism.PrismModes.RAINBOW))
            );
        }
        else if(robotBase.chassisSubsystem.isInFarZone()){
            addCommands(
                    new InstantCommand(()->robotBase.prismSubsystem.setPosition(Prism.PrismModes.LAUNCH)),
                    new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
                    new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                    new WaitCommand(50),
                    new InstantCommand(()->robotBase.intakeTransferSubsystem.transfer(0.6)),
                    new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(-0.6)),
                    new InstantCommand(()->robotBase.prismSubsystem.setPosition(Prism.PrismModes.RAINBOW)),
                    new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE))
            );
        }
    }
}
