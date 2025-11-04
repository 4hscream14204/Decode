package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchOneGreen extends SequentialCommandGroup {
    public LaunchOneGreen(RobotBase robotBase){
        addCommands(
                new LaunchCommandGroup(robotBase),
                new WaitUntilCommand(()->robotBase.launcherSubsystem.getVelocity() >= robotBase.launcherSubsystem.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5))),
                new TransferGreenBallCommandGroup(robotBase)
        );
    }
}
