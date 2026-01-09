package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Tilt;

public class ToggleTiltCommandGroup extends SequentialCommandGroup {
    public ToggleTiltCommandGroup(RobotBase robotBase){
        if(robotBase.tiltSubsystem.tiltPositionL == Tilt.Position.LEFTINACTIVE){
            addCommands(
                    new InstantCommand(()->robotBase.tiltSubsystem.setPosition(Tilt.Position.LEFTACTIVE, Tilt.Position.RIGHTACTIVE))
            );
        }
        else{
            addCommands(
                    new InstantCommand(()->robotBase.tiltSubsystem.setPosition(Tilt.Position.LEFTINACTIVE, Tilt.Position.RIGHTINACTIVE))
            );
        }
    }
}
