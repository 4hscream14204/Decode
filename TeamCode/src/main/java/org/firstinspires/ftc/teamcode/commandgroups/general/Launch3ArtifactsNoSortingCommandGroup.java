package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class Launch3ArtifactsNoSortingCommandGroup extends SequentialCommandGroup {
    RobotBase robotBase;
    public Launch3ArtifactsNoSortingCommandGroup(RobotBase m_robotBase){
        robotBase = m_robotBase;
    }
    @Override
    public void initialize(){
        addCommands(
                new LaunchCommandGroup(robotBase),
                new WaitUntilCommand(()->robotBase.launcherSubsystemLeft.isAtSpeed()),
                new WaitCommand(200),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                //new WaitCommand(1000),
                //new SetAllVelocityCommandGroup(robotBase, 0),
                new InstantCommand(()->robotBase.chassisSubsystem.bolSnapToTarget = false)
        );
        super.initialize();
    }
}
