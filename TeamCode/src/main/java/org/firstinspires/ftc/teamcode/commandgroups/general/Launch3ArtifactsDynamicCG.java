package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class Launch3ArtifactsDynamicCG extends CommandBase {
    RobotBase robotBase;
    Pose goalPose;
    Follower follower;
    public Launch3ArtifactsDynamicCG(RobotBase m_robotBase, Follower m_follower){
        robotBase = m_robotBase;
        follower = m_follower;
    }
    @Override
    public void initialize(){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED) {
                    goalPose = new Pose(127.7, 131.7);
                    new WaitUntilCommand(() -> robotBase.launcherSubsystemLeft.isAtSpeed(robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose)));
                    new Transfer3BallsNoCameraCommandGroup(robotBase);
        }
        else{
                    goalPose = new Pose(127.7, 131.7).mirror();
                    robotBase.launcherSubsystemLeft.isAtSpeed(robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose));
                    new Transfer3BallsNoCameraCommandGroup(robotBase);
        }
        super.initialize();
    }
}
