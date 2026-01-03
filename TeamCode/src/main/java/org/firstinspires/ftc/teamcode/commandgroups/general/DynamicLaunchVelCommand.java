package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class DynamicLaunchVelCommand extends CommandBase {
    RobotBase robotBase;
    Follower follower;
    public DynamicLaunchVelCommand(RobotBase m_robotBase, Follower m_follower){
        robotBase = m_robotBase;
        follower = m_follower;
    }
    @Override
    public void initialize(){
        robotBase.launcherSubsystemLeft.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower));
        robotBase.launcherSubsystemMiddle.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower));
        robotBase.launcherSubsystemRight.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower));
    }
}
