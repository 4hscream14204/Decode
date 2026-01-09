package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.IntakeBlocker;

public class ToggleIntakeBlockerCG extends SequentialCommandGroup {
    public ToggleIntakeBlockerCG(RobotBase robotBase){
        if(robotBase.intakeBlockerSubsystem.getPosition() == IntakeBlocker.Position.CLOSED){
            addCommands(
                    new InstantCommand(()->robotBase.intakeBlockerSubsystem.setPosition(IntakeBlocker.Position.OPEN))
            );
        }
        else{
            addCommands(
                    new InstantCommand(()->robotBase.intakeBlockerSubsystem.setPosition(IntakeBlocker.Position.CLOSED))
            );
        }
    }
}
