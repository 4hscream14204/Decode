package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchPatternCommandGroup extends SequentialCommandGroup {
    TransferPatternCommandGroup transferPatternCommandGroup;
    public LaunchPatternCommandGroup(RobotBase robotBase){
        transferPatternCommandGroup = new TransferPatternCommandGroup(robotBase);
        addCommands(
                new LaunchCommandGroup(robotBase),
                new WaitUntilCommand(()->robotBase.launcherSubsystemLeft.getVelocity() >= robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance())&&
                        robotBase.launcherSubsystemMiddle.getVelocity() >= robotBase.launcherSubsystemMiddle.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance())&&
                        robotBase.launcherSubsystemRight.getVelocity() >= robotBase.launcherSubsystemRight.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance())),
                new InstantCommand(()->transferPatternCommandGroup.schedule()),
                new WaitCommand(4500),
                new SetAllVelocityCommandGroup(robotBase, 0),
                new InstantCommand(()->robotBase.chassisSubsystem.bolSnapToTarget = false)
        );
    }
}
