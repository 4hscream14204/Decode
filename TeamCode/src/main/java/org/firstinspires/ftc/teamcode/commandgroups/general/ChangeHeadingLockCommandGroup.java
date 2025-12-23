package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

public class ChangeHeadingLockCommandGroup extends SequentialCommandGroup {
    public ChangeHeadingLockCommandGroup(RobotBase robotBase) {
        addCommands(
                new InstantCommand(()-> robotBase.chassisSubsystem.bolSnapToTarget = !robotBase.chassisSubsystem.bolSnapToTarget)
                //new SetAllVelocityCommandGroup(robotBase, 1500)
        );
    }

}
