package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Limelight;
import org.firstinspires.ftc.teamcode.PoseGenerator;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name = "Get Ball Pose")
public class GetBallPose extends OpMode {

    Limelight limelight;
    Limelight3A limelight3A;
    PoseGenerator poseGenerator;
    GoBildaPinpointDriver pinpoint;
    Follower follower;
    Pose startPose = new Pose(72, 72, Math.toRadians(0));

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight = new Limelight(limelight3A, 0);
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        poseGenerator = new PoseGenerator(limelight, follower);
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
    }

    @Override
    public void loop() {
        follower.update();
        pinpoint.update();
        limelight.updateLimelight();
        telemetry.addData("Ball Pose X", poseGenerator.generatePose().getX());
        telemetry.addData("Ball Pose Y", poseGenerator.generatePose().getY());
    }
}
