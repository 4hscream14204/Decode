package org.firstinspires.ftc.teamcode.pedropathing.poses;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.subsystems.Limelight;

public class PoseGenerator {
    Limelight limelight;
    Follower follower;
    double distance;
    double botHeading;
    double botPoseX;
    double botPoseY;

    public PoseGenerator(Limelight m_limelight, Follower m_follower){
        limelight = m_limelight;
        follower = m_follower;
    }

    public Pose calculatePose(){
        distance = limelight.getTargetZ();
        botHeading = follower.getHeading();
        botPoseX = follower.getPose().getX();
        botPoseY = follower.getPose().getY();

        return new Pose( (botPoseX + distance * Math.cos(botHeading)), (botPoseY + distance * Math.sin(botHeading)) );
    }
}
