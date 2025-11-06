package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class AutoTransferAndLaunchCommandGroup extends SequentialCommandGroup {
    RobotBase robotBase;
    double velocity;

    public AutoTransferAndLaunchCommandGroup(RobotBase m_robotBase, double m_velocity){
        //transferPatternCommandGroup = new TransferPatternCommandGroup(robotBase);
       robotBase = m_robotBase;
       velocity = m_velocity;

    }
    @Override
    public void initialize() {
        addCommands(
                new LaunchNoLimelightCommandGroup(robotBase, velocity),
                new WaitCommand(1000),
                new TransferPatternCommandGroup(robotBase)
        );

        //Have to call the super classes initalize as that is what tells the scheduler to run them
        super.initialize();
    }
}
