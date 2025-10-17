package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class TransferResetCommandGroup extends SequentialCommandGroup {
    public TransferResetCommandGroup(SorterServo sorterServoL, SorterServo sorterServoM, SorterServo sorterServoR){
        addCommands(
                new InstantCommand(()->sorterServoL.setPosition(SorterServo.ServoPosition.STABLE)),
                new InstantCommand(()->sorterServoM.setPosition(SorterServo.ServoPosition.STABLE)),
                new InstantCommand(()->sorterServoR.setPosition(SorterServo.ServoPosition.STABLE))
        );
    }
}
