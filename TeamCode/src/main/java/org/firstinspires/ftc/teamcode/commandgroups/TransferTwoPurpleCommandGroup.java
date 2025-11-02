package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class TransferTwoPurpleCommandGroup extends SequentialCommandGroup {
    public TransferTwoPurpleCommandGroup(RobotBase robotBase) {
        if (robotBase.sorterCameraSubsystem.hasTwoPurple() && robotBase.sorterCameraSubsystem.isMiddleAndLeftPurple) {
            addCommands(
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(750),
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME)),
                    new WaitCommand(500),
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME))
            );
        } else if (robotBase.sorterCameraSubsystem.hasTwoPurple() && robotBase.sorterCameraSubsystem.isMiddleAndRightPurple) {
            addCommands(
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(750),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME)),
                    new WaitCommand(500),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME))
            );
        } else if (robotBase.sorterCameraSubsystem.hasTwoPurple() && robotBase.sorterCameraSubsystem.isRightAndLeftPurple) {
            addCommands(
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(750),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME)),
                    new WaitCommand(500),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME))
            );
        }
        }
    }
