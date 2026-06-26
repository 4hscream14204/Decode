package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

public class PoseGenerator {

    Limelight limelight;
    Follower follower;
    double distance;
    double botHeading;
    double botPoseX;
    double botPoseY;
    double newX;
    double newY;

    public PoseGenerator(Limelight m_limelight, Follower m_follower){
        limelight = m_limelight;
        follower = m_follower;
    }

    public Pose generatePose(){
        distance = limelight.getDistance();
        botHeading = follower.getHeading();
        botPoseX = follower.getPose().getX();
        botPoseY = follower.getPose().getY();

        newX = ((Math.cos(botHeading - Math.toRadians(limelight.result.getTx())) * distance) + botPoseX);
        newY = ((Math.sin(botHeading - Math.toRadians(limelight.result.getTx())) * distance) + botPoseY);

        return new Pose(newX, newY);
    }
}
