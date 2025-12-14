package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class TransferBallInSorterRightCommandGroup extends SequentialCommandGroup {
    public TransferBallInSorterRightCommandGroup(RobotBase robotBase){
        addCommands(
                new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                new WaitCommand(500),
                new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME))
        );
    }
}
