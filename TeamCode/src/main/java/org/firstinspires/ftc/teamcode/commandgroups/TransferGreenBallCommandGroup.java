package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class TransferGreenBallCommandGroup extends SequentialCommandGroup {
    public TransferGreenBallCommandGroup(RobotBase robotBase){
        if(robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            addCommands(
                    new InstantCommand(()-> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(1000),
                    new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME))
            );
        }
        else if(robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            addCommands(
                    new InstantCommand(()-> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(1000),
                    new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME))
            );
        }
        else if(robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            addCommands(
                    new InstantCommand(()-> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(1000),
                    new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME))

            );
        }
    }
}
