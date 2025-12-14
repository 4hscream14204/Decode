package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class RGBLightLeftColorCommandGroup extends SequentialCommandGroup {
    public RGBLightLeftColorCommandGroup(RobotBase robotBase){
        if(robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))
            );
        }
        else if(robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE){
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))
            );
        }
        else{
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.RED))
            );
        }
    }
}
