package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class SetAllLaunchVelocityCommandGroup extends SequentialCommandGroup {
    public SetAllLaunchVelocityCommandGroup(RobotBase robotBase, double launchVelocity){
        addCommands(
                new ParallelCommandGroup(
                        new InstantCommand(()->robotBase.launcherSubsystemLeft.setLaunchVelocity(launchVelocity)),
                        new InstantCommand(()->robotBase.launcherSubsystemMiddle.setLaunchVelocity(launchVelocity)),
                        new InstantCommand(()->robotBase.launcherSubsystemRight.setLaunchVelocity(launchVelocity))
                )
        );
    }
}
