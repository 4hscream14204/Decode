package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class Launch3ArtifactsNoSortingFollowerCG extends SequentialCommandGroup {
    RobotBase robotBase;
    Follower follower;
    public Launch3ArtifactsNoSortingFollowerCG(RobotBase m_robotBase, Follower m_follower){
        robotBase = m_robotBase;
        follower = m_follower;
    }
    @Override
    public void initialize(){
        addCommands(
                new LaunchCommandGroup(robotBase),
                new WaitUntilCommand(()->robotBase.launcherSubsystemLeft.isAtSpeed(robotBase.limelightSubsystem.getHorizontalDistance(follower))&&
                        robotBase.launcherSubsystemMiddle.isAtSpeed(robotBase.limelightSubsystem.getHorizontalDistance(follower))&&
                        robotBase.launcherSubsystemRight.isAtSpeed(robotBase.limelightSubsystem.getHorizontalDistance(follower))),
                new WaitCommand(200),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                //new WaitCommand(1000),
                //new SetAllVelocityCommandGroup(robotBase, 0),
                new InstantCommand(()->robotBase.chassisSubsystem.bolSnapToTarget = false)
        );
        super.initialize();
    }
}