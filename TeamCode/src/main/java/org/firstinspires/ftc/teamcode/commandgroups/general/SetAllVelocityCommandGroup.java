package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class SetAllVelocityCommandGroup extends SequentialCommandGroup {
    public SetAllVelocityCommandGroup(RobotBase robotBase, double launchVelocity){
        addCommands(
                new ParallelCommandGroup(
                        new InstantCommand(()->robotBase.launcherSubsystemLeft.setVelocity(launchVelocity)),
                        new InstantCommand(()->robotBase.launcherSubsystemMiddle.setVelocity(launchVelocity)),
                        new InstantCommand(()->robotBase.launcherSubsystemRight.setVelocity(launchVelocity))
                )
        );
    }
}
