package org.firstinspires.ftc.teamcode.commandgroups;

import android.provider.ContactsContract;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class TransferPatternCommandGroup extends SequentialCommandGroup {
    public TransferPatternCommandGroup(SorterCamera camera, SorterServo sorterServo, SorterServo sorterServoM, SorterServo sorterServoR){
        if(DataStorage.pattern == DecodeEnums.Patterns.PPG){
            addCommands(
                    new TransferTwoPurpleCommandGroup(camera, sorterServo, sorterServoM, sorterServoR),
                    new WaitCommand(1000),
                    new TransferGreenBallCommandGroup(camera, sorterServo, sorterServoM, sorterServoR),
                    new WaitCommand(1000),
                    new TransferResetCommandGroup(sorterServo, sorterServoM, sorterServoR)
            );
        }
        else if (DataStorage.pattern == DecodeEnums.Patterns.GPP){
            addCommands(
            new TransferGreenBallCommandGroup(camera, sorterServo, sorterServoM, sorterServoR),
            new WaitCommand(1000),
            new TransferTwoPurpleCommandGroup(camera, sorterServo, sorterServoM, sorterServoR));
        }
    }
}
