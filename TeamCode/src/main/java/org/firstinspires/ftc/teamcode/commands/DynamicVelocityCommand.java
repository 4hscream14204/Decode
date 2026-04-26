package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
import org.firstinspires.ftc.teamcode.base.RobotBase;

public class DynamicVelocityCommand extends CommandBase {
    RobotBase robotBase;
    Follower follower;
    Pose goalPose;
    Pose futurePose;
    double distance;
    double newDistance;
    double timeOfFlightMultiplier = 0.001;
    double xSpeed;
    double ySpeed;
    double timeOfFlight;
    public DynamicVelocityCommand(RobotBase m_robotBase, Follower m_follower){
        robotBase = m_robotBase;
        follower = m_follower;
    }
    @Override
    public void execute(){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            goalPose = new Pose(144, 138);
        }
        else{
            goalPose = new Pose(144, 138).mirror();
        }
            xSpeed = robotBase.chassisSubsystem.pinpoint.getVelX(DistanceUnit.INCH);
            ySpeed = robotBase.chassisSubsystem.pinpoint.getVelY(DistanceUnit.INCH);
            distance = follower.getPose().distanceFrom(goalPose);
            timeOfFlight = distance * timeOfFlightMultiplier;
            futurePose = new Pose((goalPose.getX() - (xSpeed * timeOfFlight)), (goalPose.getY() - (ySpeed * timeOfFlight)));
            newDistance = follower.getPose().distanceFrom(futurePose);
            robotBase.launcherSubsystem.setLaunchVelocity(newDistance);
        }

    @Override
    public boolean isFinished(){
        return false;
    }
    }
