package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class TransferPedroCG extends CommandBase {
    RobotBase robotBase;
    Follower follower;
    Pose goalPose;
    public TransferPedroCG(RobotBase m_robotBase, Follower m_follower){
        robotBase = m_robotBase;
        follower = m_follower;
    }
    @Override
    public void initialize(){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED) {
            goalPose = new Pose(127.7, 131.7);
            new WaitUntilCommand(() -> robotBase.launcherSubsystemLeft.isAtSpeed(robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose))));
            new Transfer3BallsNoCameraCommandGroup(robotBase);
        }
        else{
            goalPose = new Pose(127.7, 131.7).mirror();
            robotBase.launcherSubsystemLeft.isAtSpeed(robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose)));
            new Transfer3BallsNoCameraCommandGroup(robotBase);
        }
        super.initialize();
    }
}
