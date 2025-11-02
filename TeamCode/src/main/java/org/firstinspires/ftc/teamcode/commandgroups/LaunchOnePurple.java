package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchOnePurple extends SequentialCommandGroup {
    public LaunchOnePurple(RobotBase robotBase){
        addCommands(
                new LaunchCommandGroup(robotBase),
                new TransferPurpleBallCommandGroup(robotBase)
        );
    }
}
