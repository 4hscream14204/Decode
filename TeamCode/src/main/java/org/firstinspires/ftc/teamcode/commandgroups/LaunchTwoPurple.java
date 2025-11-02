package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchTwoPurple extends SequentialCommandGroup {
    public LaunchTwoPurple(RobotBase robotBase){
        addCommands(
                new LaunchCommandGroup(robotBase),
                new TransferTwoPurpleCommandGroup(robotBase)
        );
    }
}
