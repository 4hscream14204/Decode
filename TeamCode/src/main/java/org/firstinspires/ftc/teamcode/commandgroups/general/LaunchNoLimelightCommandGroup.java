package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchNoLimelightCommandGroup extends SequentialCommandGroup {
    public LaunchNoLimelightCommandGroup(RobotBase robotBase, double m_velocity){
        addCommands(
                new SetAllVelocityCommandGroup(robotBase, m_velocity)
        );
    }
}
