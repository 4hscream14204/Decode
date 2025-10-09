package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class EjectBallInSorterCommandGroup extends SequentialCommandGroup{
    public EjectBallInSorterCommandGroup(SorterServo sorterSubsystem){
        addCommands(
                new InstantCommand(()->sorterSubsystem.setPosition(SorterServo.ServoPosition.EJECT)),
                new WaitCommand(500),
                new InstantCommand(()->sorterSubsystem.setPosition(SorterServo.ServoPosition.STABLE))
        );
    }
}
