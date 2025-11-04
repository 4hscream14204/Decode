package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchTwoPurple extends SequentialCommandGroup {
    public LaunchTwoPurple(RobotBase robotBase){
        addCommands(
                new LaunchCommandGroup(robotBase),
                new WaitUntilCommand(()->robotBase.launcherSubsystem.getVelocity() >= robotBase.launcherSubsystem.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5))),
                new TransferTwoPurpleCommandGroup(robotBase)
        );
    }
}
