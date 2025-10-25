package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class TransferPurpleBallCommandGroup extends SequentialCommandGroup {
    public TransferPurpleBallCommandGroup(SorterCamera sorterCamera, SorterServo sorterServo, SorterServo sorterServoM, SorterServo sorterServoR){
        if(sorterCamera.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE){
            addCommands(
                   new InstantCommand(()->sorterServo.setPosition(SorterServo.ServoPosition.TEST1))
            );
        }
        else if(sorterCamera.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE){
            addCommands(
                    new InstantCommand(()->sorterServoM.setPosition(SorterServo.ServoPosition.TEST2))
            );
        }
        else if(sorterCamera.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE){
            addCommands(
                    new InstantCommand(()->sorterServoR.setPosition(SorterServo.ServoPosition.TEST3))
            );
        }
    }
}
