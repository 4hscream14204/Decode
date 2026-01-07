package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.skeletonarmy.marrow.zones.Zone;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class Launch3ArtifactsNoSortingCommandGroup extends SequentialCommandGroup {
    RobotBase robotBase;
    Zone robotZone;
    Zone closeZone;
    Zone farZone;
    public Launch3ArtifactsNoSortingCommandGroup(RobotBase m_robotBase, Zone m_robotZone, Zone m_closeZone, Zone m_farZone){
        robotBase = m_robotBase;
        robotZone = m_robotZone;
        closeZone = m_closeZone;
        farZone = m_farZone;
    }
    @Override
    public void initialize(){
        if(robotZone.isInside(farZone) || robotZone.isInside(closeZone)) {
            addCommands(
                    new LaunchCommandGroup(robotBase),
                    new WaitUntilCommand(() -> robotBase.launcherSubsystemLeft.getVelocity() >= robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5)) &&
                            robotBase.launcherSubsystemMiddle.getVelocity() >= robotBase.launcherSubsystemMiddle.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5)) &&
                            robotBase.launcherSubsystemRight.getVelocity() >= robotBase.launcherSubsystemRight.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5))),
                    new WaitCommand(200),
                    new Transfer3BallsNoCameraCommandGroup(robotBase),
                    new WaitCommand(1000),
                    new SetAllVelocityCommandGroup(robotBase, 0),
                    new InstantCommand(() -> robotBase.chassisSubsystem.bolSnapToTarget = false)
            );
        }
        else{
            addCommands(
                    new InstantCommand(()->robotBase.intakeSubsystem.intake(1))
            );
        }
        super.initialize();
    }
}
