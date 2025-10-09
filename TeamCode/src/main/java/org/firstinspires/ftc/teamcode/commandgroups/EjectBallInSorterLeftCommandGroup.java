package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class EjectBallInSorterLeftCommandGroup extends SequentialCommandGroup {
    public EjectBallInSorterLeftCommandGroup(RobotBase robotBase){
        addCommands(
                new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.EJECT)),
                new WaitCommand(500),
                new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.STABLE))
        );
    }
}
