package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;


public class LaunchCommandGroup extends SequentialCommandGroup {
    public LaunchCommandGroup(RobotBase robotBase){
        addCommands(
                new InstantCommand(()->robotBase.launcherSubsystem.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5))));


    }
}
