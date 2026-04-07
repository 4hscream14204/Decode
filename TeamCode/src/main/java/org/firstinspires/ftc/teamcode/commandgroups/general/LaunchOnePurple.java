package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchOnePurple extends SequentialCommandGroup {
    public LaunchOnePurple(RobotBase robotBase){
        if(robotBase.limelightSubsystem.goalInSight()) {
            addCommands(
                    new LaunchCommandGroup(robotBase),
                    new WaitUntilCommand(() -> robotBase.launcherSubsystemLeft.getVelocity() >= robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5)) &&
                            robotBase.launcherSubsystemMiddle.getVelocity() >= robotBase.launcherSubsystemMiddle.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5)) &&
                            robotBase.launcherSubsystemRight.getVelocity() >= robotBase.launcherSubsystemRight.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5))),
                    new TransferPurpleBallCommandGroup(robotBase)
            );
        }
    }
}
