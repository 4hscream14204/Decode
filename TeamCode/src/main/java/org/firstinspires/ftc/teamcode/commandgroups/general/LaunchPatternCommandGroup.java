package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchPatternCommandGroup extends SequentialCommandGroup {
    TransferPatternCommandGroup transferPatternCommandGroup;
    public LaunchPatternCommandGroup(RobotBase robotBase){

        if(robotBase.limelightSubsystem.goalInSight()) {
            transferPatternCommandGroup = new TransferPatternCommandGroup(robotBase);
            addCommands(
                    new LaunchCommandGroup(robotBase),
                    new WaitUntilCommand(() -> robotBase.launcherSubsystemLeft.getVelocity() >= robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5)) &&
                            robotBase.launcherSubsystemMiddle.getVelocity() >= robotBase.launcherSubsystemMiddle.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5)) &&
                            robotBase.launcherSubsystemRight.getVelocity() >= robotBase.launcherSubsystemRight.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5))),
                    new InstantCommand(() -> transferPatternCommandGroup.schedule()),
                    new WaitCommand(4500),
                    new SetAllVelocityCommandGroup(robotBase, 0),
                    new InstantCommand(() -> robotBase.chassisSubsystem.bolSnapToTarget = false)
            );
        }
    }
}
