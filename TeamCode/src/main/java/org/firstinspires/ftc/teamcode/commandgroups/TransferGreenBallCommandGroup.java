package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class TransferGreenBallCommandGroup extends SequentialCommandGroup {
    public TransferGreenBallCommandGroup(SorterCamera camera, SorterServo sorterServoL, SorterServo sorterServoM, SorterServo sorterServoR){
        if(camera.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            addCommands(
                    new InstantCommand(()-> sorterServoL.setPosition(SorterServo.ServoPosition.TEST3))
            );
        }
        else if(camera.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            addCommands(
                    new InstantCommand(()-> sorterServoM.setPosition(SorterServo.ServoPosition.TEST3))
            );
        }
        else if(camera.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            addCommands(
                    new InstantCommand(()-> sorterServoR.setPosition(SorterServo.ServoPosition.TEST3))
            );
        }
    }
}
