package org.firstinspires.ftc.teamcode.opmode.auto;

import static org.firstinspires.ftc.teamcode.pedropathing.Tuning.drawCurrent;
import static org.firstinspires.ftc.teamcode.pedropathing.Tuning.drawCurrentAndHistory;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.opencv.core.Mat;

@Autonomous(name = "Decode Test")
public class DecodeTestRoute extends OpMode {
    Follower follower;
    private PathChain path1;
    public SequentialCommandGroup followRoute;
    private final Pose startPose = new Pose(56, 8, Math.toRadians(0));

    /*public void buildPaths(){
        //follower.setStartingPose(startPose);
        path1 = follower.pathBuilder()
                .addPath(new BezierCurve(startPose, new Pose(68.179, 108.000), new Pose(42.838, 84.067), new Pose(27.553, 123.285)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))
                .build();
    }*/

    @Override
    public void init(){
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        //follower.setStartingPose(startPose);
        //CommandScheduler.getInstance().reset();
        //buildPaths();
        /*new BezierCurve(
                                new Pose(56.000, 8.000),
                                new Pose(68.179, 108.000),
                                new Pose(42.838, 84.067),
                                new Pose(27.553, 123.285)
                        )
                )
                //.setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))
                .build();*/
        /*followRoute = new SequentialCommandGroup(
                new FollowPath(follower, path1, true, 1)
        );*/
    }

    @Override
    public void init_loop(){
        //follower.update();
        //drawCurrent();
    }

    @Override
    public void start(){
        //follower.setStartingPose(startPose);
        path1 = follower.pathBuilder()
                .addPath(new BezierCurve(startPose, new Pose(68.179, 108.000), new Pose(42.838, 84.067), new Pose(27.553, 123.285)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))
                .build();
        follower.followPath(path1);
    }

    @Override
    public void loop(){
        follower.update();
        //drawCurrentAndHistory();
    }
}
