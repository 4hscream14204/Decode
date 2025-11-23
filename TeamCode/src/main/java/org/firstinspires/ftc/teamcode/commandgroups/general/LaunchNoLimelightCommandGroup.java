package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchNoLimelightCommandGroup extends SequentialCommandGroup {
    public LaunchNoLimelightCommandGroup(RobotBase robotBase, double m_velocity){
        addCommands(
                new SetAllVelocityCommandGroup(robotBase, m_velocity)
        );
    }
}
