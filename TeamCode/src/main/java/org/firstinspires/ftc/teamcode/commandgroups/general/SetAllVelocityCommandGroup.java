package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

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
