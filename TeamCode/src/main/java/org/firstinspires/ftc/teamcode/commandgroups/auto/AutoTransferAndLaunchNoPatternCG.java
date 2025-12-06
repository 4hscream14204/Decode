package org.firstinspires.ftc.teamcode.commandgroups.auto;

import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchNoLimelightCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferPatternCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class AutoTransferAndLaunchNoPatternCG extends SequentialCommandGroup {
    RobotBase robotBase;
    double velocity;

    public AutoTransferAndLaunchNoPatternCG(RobotBase m_robotBase, double m_velocity){
        //transferPatternCommandGroup = new TransferPatternCommandGroup(robotBase);
       robotBase = m_robotBase;
       velocity = m_velocity;

    }
    @Override
    public void initialize() {
        addCommands(
                new LaunchNoLimelightCommandGroup(robotBase, velocity),
                new WaitCommand(500),
                new Transfer3BallsNoCameraCommandGroup(robotBase)
        );

        //Have to call the super classes initalize as that is what tells the scheduler to run them
        super.initialize();
    }
}
