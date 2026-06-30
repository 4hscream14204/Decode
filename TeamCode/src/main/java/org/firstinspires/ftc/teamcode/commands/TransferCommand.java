package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
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
        if(DataStorage.launchingMode == DecodeEnums.LaunchingMode.GOAL) {
            if (robotBase.chassisSubsystem.isInFarZone()) {
                addCommands(
                    /*new InstantCommand(()->robotBase.prismSubsystem.setPosition(Prism.PrismModes.LAUNCH)),
                    new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                    new WaitCommand(250),
                    new InstantCommand(()->robotBase.intakeTransferSubsystem.transfer(0.4)),
                    new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(-0.4)),
                    new WaitCommand(750),
                    new ParallelCommandGroup(new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),new InstantCommand(()->robotBase.intakeTransferSubsystem.stopAll())),
                    new InstantCommand(()->robotBase.prismSubsystem.setPosition(Prism.PrismModes.RAINBOW)),
                    new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE))*/
                        new InstantCommand(() -> robotBase.prismSubsystem.setMode(Prism.PrismModes.LAUNCH, false)),
                        new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                        new InstantCommand(() -> robotBase.intakeTransferSubsystem.intakeAndTransfer(0.4)),
                        new WaitCommand(750),
                        new ParallelCommandGroup(new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)), new InstantCommand(() -> robotBase.intakeTransferSubsystem.stopAll())),
                        new InstantCommand(()-> robotBase.prismSubsystem.setGamePhase(time)),
                        new InstantCommand(() -> robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE))
                );
            } else {
                addCommands(
                        new WaitUntilCommand(() -> robotBase.launcherSubsystem.isAtSpeed()),
                        new ParallelCommandGroup(
                                new InstantCommand(() -> robotBase.prismSubsystem.setMode(Prism.PrismModes.LAUNCH, false)),
                                new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE))),
                        new WaitCommand(50),
                        new ParallelCommandGroup(
                                new InstantCommand(() -> robotBase.intakeTransferSubsystem.transfer(0.55)),
                                new InstantCommand(() -> robotBase.intakeTransferSubsystem.intake(-1))),
                        new WaitCommand(1000),
                        new ParallelCommandGroup(
                                new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                                new InstantCommand(() -> robotBase.intakeTransferSubsystem.stopAll()),
                                new InstantCommand(() -> robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)))
                );
            }
        }
        else{
            addCommands(
                    new WaitUntilCommand(() -> robotBase.launcherSubsystem.isAtSpeed()),
                    new ParallelCommandGroup(
                            new InstantCommand(() -> robotBase.prismSubsystem.setMode(Prism.PrismModes.LAUNCH, false)),
                            new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE))),
                    new WaitCommand(50),
                    new ParallelCommandGroup(
                            new InstantCommand(() -> robotBase.intakeTransferSubsystem.transfer(0.35)),
                            new InstantCommand(() -> robotBase.intakeTransferSubsystem.intake(-0.75))),
                    new WaitCommand(750),
                    new ParallelCommandGroup(
                            new InstantCommand(()->robotBase.intakeTransferSubsystem.transfer(-0.3)),
                            new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(0.3))),
                    new WaitCommand(500),
                    new ParallelCommandGroup(
                            new InstantCommand(() -> robotBase.intakeTransferSubsystem.transfer(0.55)),
                            new InstantCommand(() -> robotBase.intakeTransferSubsystem.intake(-0.75))),
                    new WaitCommand(500),
                    new ParallelCommandGroup(
                            new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                            new InstantCommand(() -> robotBase.intakeTransferSubsystem.stopAll()),
                            new InstantCommand(() -> robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)))
            );
        }
    }
}
