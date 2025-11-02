package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchOneGreen extends SequentialCommandGroup {
    public LaunchOneGreen(RobotBase robotBase){
        addCommands(
                new LaunchCommandGroup(robotBase),
                new TransferGreenBallCommandGroup(robotBase)
        );
    }
}
