package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class TransferTwoPurpleCommandGroup extends SequentialCommandGroup {
    private int state;

    public void setState(int m_state) {
        state = m_state;
    }

    public TransferTwoPurpleCommandGroup(RobotBase robotBase) {
        if (robotBase.sorterCameraSubsystem.hasTwoPurple() && robotBase.sorterCameraSubsystem.isMiddleAndLeftPurple) {
            addCommands(
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.TRANSFER)),
                    new WaitCommand(750),
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.TRANSFER)),
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.STABLE)),
                    new WaitCommand(500),
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.STABLE))
            );
        } else if (robotBase.sorterCameraSubsystem.hasTwoPurple() && robotBase.sorterCameraSubsystem.isMiddleAndRightPurple) {
            addCommands(
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.TRANSFER)),
                    new WaitCommand(750),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.TRANSFER)),
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.STABLE)),
                    new WaitCommand(500),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.STABLE))
            );
        } else if (robotBase.sorterCameraSubsystem.hasTwoPurple() && robotBase.sorterCameraSubsystem.isRightAndLeftPurple) {
            addCommands(
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.TRANSFER)),
                    new WaitCommand(750),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.TRANSFER)),
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.STABLE)),
                    new WaitCommand(500),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.STABLE))
            );
        }
        }
    }
