package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorGoBildaPinpoint;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Tilt;

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
            //distance = robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose);
        }
        else{
            goalPose = new Pose(127.7, 131.7).mirror();
            //distance = robotBase.limelightSubsystem.getHorizontalDistance(follower, goalPose);
        }
        if (robotBase.tiltSubsystem.tiltPositionL == Tilt.Position.LEFTACTIVE) {
            robotBase.launcherSubsystemLeft.setVelocitySimple(0);
            robotBase.launcherSubsystemMiddle.setVelocitySimple(0);
            robotBase.launcherSubsystemRight.setVelocitySimple(0);
        } else {
            xSpeed = robotBase.chassisSubsystem.pinpoint.getVelX(DistanceUnit.INCH);
            ySpeed = robotBase.chassisSubsystem.pinpoint.getVelY(DistanceUnit.INCH);
            distance = follower.getPose().distanceFrom(goalPose) * 2.54;
            timeOfFlight = distance * timeOfFlightMultiplier;
            futurePose = new Pose((goalPose.getX() - (xSpeed * timeOfFlight)), (goalPose.getY() - (ySpeed * timeOfFlight)));
            newDistance = follower.getPose().distanceFrom(futurePose) * 2.54;
            robotBase.launcherSubsystemLeft.setLaunchVelocity(newDistance);
            robotBase.launcherSubsystemMiddle.setLaunchVelocity(newDistance);
            robotBase.launcherSubsystemRight.setLaunchVelocity(newDistance);
        }
    }
/*
    @Override
    public boolean isFinished() {
        if (robotBase.tiltSubsystem.tiltPositionL == Tilt.Position.LEFTACTIVE) {
            new SetAllVelocityCommandGroup(robotBase, 0);
            return true;
        } else {
            return false;
        }
    }*/
}
