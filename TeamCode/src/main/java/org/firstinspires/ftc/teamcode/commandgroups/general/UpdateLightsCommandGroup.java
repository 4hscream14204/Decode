package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class UpdateLightsCommandGroup extends SequentialCommandGroup {
    public UpdateLightsCommandGroup(RobotBase robotBase) {
        if (robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE) {
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))
            );
        } else if(robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN) {
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))
            );
        } else {
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))
            );
        }

        if (robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE) {
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))
            );
        } else if (robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN) {
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))
            );
        } else {
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))
            );
        }

        if (robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE) {
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))
            );
        } else if (robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN) {
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))
            );
        } else {
            addCommands(
                    new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))
            );
        }

    }
}
