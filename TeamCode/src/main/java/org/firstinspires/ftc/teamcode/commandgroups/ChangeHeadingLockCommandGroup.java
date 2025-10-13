package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

public class ChangeHeadingLockCommandGroup extends SequentialCommandGroup {
    boolean isBlueAlliance = true;
    boolean lookingAtArtifacts = true;

    public ChangeHeadingLockCommandGroup(RobotBase robotBase){
        if(lookingAtArtifacts){
            addCommands(
                    new InstantCommand(()->robotBase.limelightSubsystem.changePipeline(Limelight.limelightPipelines.PURPLEARTIFACT))
            );
        }
        else if(isBlueAlliance && !lookingAtArtifacts){
            addCommands(
                    new InstantCommand(()->robotBase.limelightSubsystem.changePipeline(Limelight.limelightPipelines.BLUEGOAL))
            );
        }
        if (!isBlueAlliance && !lookingAtArtifacts){
            addCommands(
                    new InstantCommand(()->robotBase.limelightSubsystem.changePipeline(Limelight.limelightPipelines.REDGOAL))
            );
        }

    }
}
