package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class PreloadThreeArtifactsCommandGroup extends SequentialCommandGroup {
    public PreloadThreeArtifactsCommandGroup(RobotBase robotBase){
        addCommands(
                new WaitCommand(500),
                new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.PRELOAD)),
                new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.PRELOAD)),
                new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.PRELOAD))
        );
    }
}
