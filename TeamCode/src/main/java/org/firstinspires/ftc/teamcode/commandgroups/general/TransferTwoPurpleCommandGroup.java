package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class TransferTwoPurpleCommandGroup extends SequentialCommandGroup {
    RobotBase robotBase;
    boolean hasTwoPurple;

    public TransferTwoPurpleCommandGroup(RobotBase m_robotBase) {
        robotBase = m_robotBase;
        hasTwoPurple = robotBase.sorterCameraSubsystem.hasTwoPurple();
    }

    @Override
    public void initialize() {
        if (hasTwoPurple && robotBase.sorterCameraSubsystem.isMiddleAndLeftPurple) {
            addCommands(
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(500),
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME)),
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME))
            );
        } else if (hasTwoPurple && robotBase.sorterCameraSubsystem.isMiddleAndRightPurple) {
            addCommands(
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(500),
                    new InstantCommand(() -> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME)),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME))
            );
        } else if (hasTwoPurple && robotBase.sorterCameraSubsystem.isRightAndLeftPurple) {
            addCommands(
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(500),
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME)),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME))
            );
            //Have to call the super classes initalize as that is what tells the scheduler to run them
        }
        else{
            addCommands(
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(500),
                    new InstantCommand(() -> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME)),
                    new InstantCommand(() -> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME)));
        }
        super.initialize();
    }
}