package org.firstinspires.ftc.teamcode.opmode.auto;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;

import java.util.List;

@Autonomous(name = "ObeliskTrackTest")
public class TrackObeliskTest extends OpMode {
    public Limelight3A limelight3A;
    public Follower follower;
    private Pose startingPose = new Pose(56, 8, Math.toRadians(0));
    SequentialCommandGroup obelisk;
    LLResult result;
    PathChain GPP;
    PathChain PGP;
    PathChain PPG;
    PathChain notDetected;
    PathChain toGoal;
    @Override
    public void init() {
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        follower = Constants.createFollower(hardwareMap);
        limelight3A.pipelineSwitch(2);
        limelight3A.setPollRateHz(100);
        limelight3A.start();

        toGoal = follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(56.000, 8.000), new Pose(60.737, 78.838), new Pose(39.620, 117.654)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(145))
                .build();

        GPP = follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(39.62, 117.654), new Pose(46.000, 84.000)))
                .setLinearHeadingInterpolation(Math.toRadians(145), Math.toRadians(90))
                .build();

        PGP = follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(39.62, 117.654), new Pose(43.000, 60.000)))
                .setLinearHeadingInterpolation(Math.toRadians(145), Math.toRadians(90))
                .build();

        PPG = follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(39.62, 117.654), new Pose(43.000, 33.000)))
                .setLinearHeadingInterpolation(Math.toRadians(145), Math.toRadians(90))
                .build();

        notDetected = follower.pathBuilder()
                .addPath(new BezierLine(new Pose(56.000, 8.000), new Pose(56.000, 40.000)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90))
                .build();
    }

    @Override
    public void init_loop() {
        result = limelight3A.getLatestResult();
        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        int id = 0;
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            id = fiducial.getFiducialId(); // The ID number of the fiducial
        }
        if(id == 21){
            obelisk = new SequentialCommandGroup(
                    new FollowPath(follower, toGoal, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, GPP, true, 1));
        }
        else if(id == 22){
            obelisk = new SequentialCommandGroup(
                    new FollowPath(follower, toGoal, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, PGP, true, 1));
        }
        else if(id == 23){
            obelisk = new SequentialCommandGroup(
                    new FollowPath(follower, toGoal, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, PPG, true, 1));
        }
        else{
            obelisk = new SequentialCommandGroup(
                    new FollowPath(follower, notDetected, true, 1)
            );
        }
        telemetry.addData("ID", id);
    }

    @Override
    public void start() {
        follower.setStartingPose(startingPose);
        CommandScheduler.getInstance().schedule(obelisk);
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        follower.update();
    }
}
