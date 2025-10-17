package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class TransferTwoPurpleCommandGroup extends SequentialCommandGroup {
    private int state;

    public void setState(int m_state) {
        state = m_state;
    }

    public TransferTwoPurpleCommandGroup(SorterCamera camera, SorterServo sorterServoL, SorterServo sorterServoM, SorterServo sorterServoR) {
        switch (state) {
            case 0:
                if (DataStorage.pattern == DecodeEnums.Patterns.PPG) {
                    if (camera.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE) {
                        addCommands(
                                new InstantCommand(() -> sorterServoL.setPosition(SorterServo.ServoPosition.TEST3))
                        );
                    }
                    if (camera.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE) {
                        addCommands(
                                new WaitCommand(500),
                                new InstantCommand(() -> sorterServoM.setPosition(SorterServo.ServoPosition.TEST2))
                        );
                    }
                    if (camera.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE) {
                        addCommands(
                                new WaitCommand(1000),
                                new InstantCommand(() -> sorterServoR.setPosition(SorterServo.ServoPosition.TEST3))
                        );
                    }
                }
                    else {
                        setState(1);
                    }
            case 1:
                setState(2);

            case 2:
                if (DataStorage.pattern == DecodeEnums.Patterns.GPP) {
                    if (camera.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE) {
                        addCommands(
                                new InstantCommand(() -> sorterServoL.setPosition(SorterServo.ServoPosition.TEST1))
                                    );
                        }
                    if (camera.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE) {
                        addCommands(
                                new WaitCommand(500),
                                new InstantCommand(() -> sorterServoM.setPosition(SorterServo.ServoPosition.TEST2))
                                    );
                                }
                                if (camera.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE) {
                                    addCommands(
                                            new WaitCommand(1000),
                                            new InstantCommand(() -> sorterServoR.setPosition(SorterServo.ServoPosition.TEST3))
                                    );
                                }
                        } else {
                            setState(-1);
                        }
                }
        }
    }
