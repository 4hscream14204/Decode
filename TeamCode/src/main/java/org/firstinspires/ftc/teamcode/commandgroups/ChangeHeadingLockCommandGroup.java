package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Chassis;

public class ChangeHeadingLockCommandGroup extends SequentialCommandGroup {
    boolean isBlueAlliance = true;
    boolean lookingAtArtiacts = true;

    public ChangeHeadingLockCommandGroup(RobotBase robotBase){
        if(!lookingAtArtiacts){
            addCommands(
                    new InstantCommand(()->robotBase.chassisSubsystem.changePipline(Chassis.limelightPiplines.PURPLEARTIFACT))
            );
        }
        else if(isBlueAlliance && lookingAtArtiacts){
            addCommands(
                    new InstantCommand(()->robotBase.chassisSubsystem.changePipline(Chassis.limelightPiplines.BLUEGOAL))
            );
        }
        if (isBlueAlliance = false && lookingAtArtiacts){
            addCommands(
                    new InstantCommand(()->robotBase.chassisSubsystem.changePipline(Chassis.limelightPiplines.REDGOAL))
            );
        }

    }
}
