package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class TransferGreenBallCommandGroup extends SequentialCommandGroup {
    RobotBase robotBase;

    public TransferGreenBallCommandGroup(RobotBase m_robotBase) {
        robotBase = m_robotBase;
    }
    @Override
    public void initialize() {
        if(robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            addCommands(
                    new InstantCommand(()-> robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(500),
                    new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME))
            );
        }
        else if(robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            addCommands(
                    new InstantCommand(()-> robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(500),
                    new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME))
            );
        }
        else if(robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            addCommands(
                    new InstantCommand(()-> robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                    new WaitCommand(500),
                    new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME))

            );
        //Have to call the super classes initalize as that is what tells the scheduler to run them

    }
        super.initialize();
        }
    }

