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
    double timeOfFlightMultiplier = 0.003/*0.003*/;
    double xSpeed;
    double ySpeed;
    double timeOfFlight;
    public DynamicVelocityCommand(RobotBase m_robotBase, Follower m_follower){
        robotBase = m_robotBase;
        follower = m_follower;
    }
    @Override
    public void initialize(){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            goalPose = new Pose(127.7, 131.7);
        }
        else{
            goalPose = new Pose(127.7, 131.7).mirror();
        }
            xSpeed = robotBase.chassisSubsystem.pinpoint.getVelX(DistanceUnit.INCH);
            ySpeed = robotBase.chassisSubsystem.pinpoint.getVelY(DistanceUnit.INCH);
            distance = follower.getPose().distanceFrom(goalPose) * 2.54;
            timeOfFlight = distance * timeOfFlightMultiplier;
            futurePose = new Pose((goalPose.getX() - (xSpeed * timeOfFlight)), (goalPose.getY() - (ySpeed * timeOfFlight)));
            newDistance = follower.getPose().distanceFrom(futurePose) * 2.54;
            robotBase.launcherSubsystem.setLaunchVelocity(newDistance);
        }
    }
