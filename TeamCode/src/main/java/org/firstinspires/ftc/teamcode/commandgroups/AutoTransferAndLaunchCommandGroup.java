package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class AutoTransferAndLaunchCommandGroup extends SequentialCommandGroup {
    public AutoTransferAndLaunchCommandGroup(RobotBase robotBase, double m_velocity){
        addCommands(
                new LaunchNoLimelightCommandGroup(robotBase, m_velocity),
                new WaitUntilCommand(()->robotBase.launcherSubsystem.getVelocity() >= m_velocity),
                new TransferPatternCommandGroup(robotBase)
        );
    }
}
