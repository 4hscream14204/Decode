package org.firstinspires.ftc.teamcode.commandgroups.auto;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchNoLimelightCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferPatternCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

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
                new WaitUntilCommand(()->robotBase.launcherSubsystemLeft.getVelocity() >= velocity &&
                        robotBase.launcherSubsystemMiddle.getVelocity() >= velocity &&
                        robotBase.launcherSubsystemRight.getVelocity() >= velocity),
                new TransferPatternCommandGroup(robotBase)
        );

        //Have to call the super classes initalize as that is what tells the scheduler to run them
        super.initialize();
    }
}
