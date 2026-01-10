package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class Transfer3BallsNoCameraCommandGroup extends SequentialCommandGroup {
    public Transfer3BallsNoCameraCommandGroup(RobotBase robotBase){
        addCommands(
                new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),
                new WaitCommand(750),
                new TransferResetCommandGroup(robotBase)
        );
    }
}
