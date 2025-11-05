package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class AutoTransferAndLaunchCommandGroup extends SequentialCommandGroup {
    public AutoTransferAndLaunchCommandGroup(RobotBase robotBase, double m_velocity){
        addCommands(
                new LaunchNoLimelightCommandGroup(robotBase, m_velocity),
                new WaitCommand(1000),
                new TransferPatternCommandGroup(robotBase)
        );
    }
}
