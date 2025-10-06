package org.firstinspires.ftc.teamcode.pedropathing.routes;


import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;

@Autonomous(name = "Big Launch Zone Red Auto")
public class LargeLaunchZoneRedRoute extends OpMode {
    Follower follower;
    SequentialCommandGroup route;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
         PathChain goesFromWallToShootPreload = follower.pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(79.000, 139.000), new Pose(73.000, 113.000), new Pose(100.000, 125.000)))
                .setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(24))
                .build();
         PathChain linesUpWithFurthestRow = follower.pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(100.000, 125.000), new Pose(77.016, 85.753), new Pose(98.049, 38.508)))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
         PathChain intakesArtifacts = follower.pathBuilder()
                .addPath(
                        new BezierLine(new Pose(98.049, 38.508), new Pose(124.908, 38.184)))
                .setTangentHeadingInterpolation()
                .build();
         PathChain goesToShoot = follower.pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(124.908, 38.184), new Pose(99.020, 40.449), new Pose(74.000, 106.000), new Pose(102.903, 124.261)))
                .setTangentHeadingInterpolation()
                .build();
         PathChain linesUpWithMiddleRow = follower.pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(102.903, 124.261), new Pose(74.000, 62.000), new Pose(103.000, 62.000)))
                .setTangentHeadingInterpolation()
                .build();
         PathChain intakesMiddleRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(new Pose(103.000, 62.000), new Pose(127.497, 61.483)))
                .setTangentHeadingInterpolation()
                .build();
         PathChain goesToShootArtifacts = follower.pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(127.497, 61.483), new Pose(79.604, 54.688), new Pose(88.018, 116.818), new Pose(102.903, 123.613)))
                .setTangentHeadingInterpolation()
                .build();
         PathChain linesUpToIntakeThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(102.903, 123.613), new Pose(79.000, 84.000), new Pose(104.000, 84.000)))
                .setTangentHeadingInterpolation()
                .build();
         PathChain intakesThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(new Pose(104.000, 84.000), new Pose(129.438, 83.811)))
                .setTangentHeadingInterpolation()
                .build();
         PathChain shootsArtifacts = follower.pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(129.438, 83.811), new Pose(75.721, 98.049), new Pose(106.000, 123.000)))
                .setTangentHeadingInterpolation()
                .build();

        follower = Constants.createFollower(hardwareMap);
        route = new SequentialCommandGroup(
                new FollowPath(follower, goesFromWallToShootPreload, true, 1),
                new FollowPath(follower, linesUpWithFurthestRow, true, 1),
                new FollowPath(follower, intakesArtifacts, true, 1),
                new FollowPath(follower, goesToShoot, true, 1),
                new FollowPath(follower, linesUpWithMiddleRow, true, 1),
                new FollowPath(follower, intakesMiddleRow, true, 1),
                new FollowPath(follower, goesToShootArtifacts, true, 1),
                new FollowPath(follower, linesUpToIntakeThirdRow, true, 1),
                new FollowPath(follower, intakesThirdRow, true, 1),
                new FollowPath(follower, shootsArtifacts, true, 1)



        );
    }

    @Override
    public void start() {
        CommandScheduler.getInstance().schedule(route);
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        follower.update();
    }
}
