package org.firstinspires.ftc.teamcode.commandgroups;

import android.provider.ContactsContract;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class TransferPatternCommandGroup extends SequentialCommandGroup {
    public TransferPatternCommandGroup(RobotBase robotBase){
        if(DataStorage.pattern == DecodeEnums.Patterns.PPG){
            addCommands(
                    new TransferTwoPurpleCommandGroup(robotBase),
                    new WaitCommand(1000),
                    new TransferGreenBallCommandGroup(robotBase),
                    new WaitCommand(1000),
                    new TransferResetCommandGroup(robotBase)
            );
        }
        else if (DataStorage.pattern == DecodeEnums.Patterns.GPP){
            addCommands(
                new TransferGreenBallCommandGroup(robotBase),
                new WaitCommand(1000),
                new TransferTwoPurpleCommandGroup(robotBase),
                new WaitCommand(1000),
                new TransferResetCommandGroup(robotBase)
            );
        }
        else if(DataStorage.pattern == DecodeEnums.Patterns.PGP){
            addCommands(
                    new TransferPurpleBallCommandGroup(robotBase),
                    new TransferGreenBallCommandGroup(robotBase),
                    new TransferPurpleBallCommandGroup(robotBase)
            );
        }
    }
}
