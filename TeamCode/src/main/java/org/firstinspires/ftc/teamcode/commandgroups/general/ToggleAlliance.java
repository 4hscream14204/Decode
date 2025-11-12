package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

public class ToggleAlliance extends SequentialCommandGroup {
    public ToggleAlliance(RobotBase robotBase){
        if(DataStorage.alliance == DecodeEnums.Alliance.BLUE){
            addCommands(
                    new InstantCommand(()->DataStorage.alliance = DecodeEnums.Alliance.RED),
                    new InstantCommand(()->robotBase.limelightSubsystem.changePipeline(Limelight.limelightPipelines.REDGOAL))
            );
        }
        else{
            addCommands(
                    new InstantCommand(()->DataStorage.alliance = DecodeEnums.Alliance.BLUE),
                    new InstantCommand(()->robotBase.limelightSubsystem.changePipeline(Limelight.limelightPipelines.BLUEGOAL))
            );
        }
    }
}
