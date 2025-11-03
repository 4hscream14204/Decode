package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchNoLimelightCommandGroup extends SequentialCommandGroup {
    public LaunchNoLimelightCommandGroup(RobotBase robotBase, double m_velocity){
        addCommands(
                new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(m_velocity))
        );
    }
}
