package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

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
