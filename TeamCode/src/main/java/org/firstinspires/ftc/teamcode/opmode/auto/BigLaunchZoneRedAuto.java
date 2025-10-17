package org.firstinspires.ftc.teamcode.opmode.auto;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;


    @Autonomous(name = "Big Launch Zone Red Auto")
    public class BigLaunchZoneRedAuto extends OpMode {
        Follower follower;
        SequentialCommandGroup route;
        Pose startPose;
        PathChain goesFromWallToShootPreload;
        PathChain linesUpWithFurthestRow;
        PathChain intakesArtifacts;
        PathChain goesToShoot;
        PathChain linesUpWithMiddleRow;
        PathChain intakesMiddleRow;
        PathChain goesToShootArtifacts;
        PathChain linesUpToIntakeThirdRow;
        PathChain intakesThirdRow;
        PathChain shootsArtifacts;
        @Override
        public void init() {
            follower = Constants.createFollower(hardwareMap);
            startPose = new Pose(85, 135);
            CommandScheduler.getInstance().reset();
            goesFromWallToShootPreload = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(85, 135), new Pose(85.000, 115.000), new Pose(90, 95)))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            //Closest line//
            linesUpToIntakeThirdRow = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(102.903, 123.613), new Pose(79.000, 84.000), new Pose(104.000, 79)))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();
            intakesThirdRow = follower.pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(104.000, 84.000), new Pose(129.438, 79)))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();
            shootsArtifacts = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(129.438, 83.811), new Pose(75.721, 98.049), new Pose(90, 95)))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            //Middle line//
            linesUpWithMiddleRow = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(102.903, 124.261), new Pose(74.000, 62.000), new Pose(103.000, 54)))
                    .setConstantHeadingInterpolation(0)
                    .build();
            intakesMiddleRow = follower.pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(103.000, 54), new Pose(127.497, 54)))
                    .setConstantHeadingInterpolation(0)
                    .build();
            goesToShootArtifacts = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(127.497, 61.483), new Pose(79.604, 54.688), new Pose(88.018, 116.818), new Pose(90, 95)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            //furthest line//
            linesUpWithFurthestRow = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(100.000, 125.000), new Pose(77.016, 85.753), new Pose(98.049, 30)))
                    .setConstantHeadingInterpolation(Math.toRadians(-90))
                    .build();
            intakesArtifacts = follower.pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(98.049, 38.508), new Pose(127, 35)))
                    .setConstantHeadingInterpolation(0)
                    .build();
            goesToShoot = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(124.908, 38.184), new Pose(99.020, 40.449), new Pose(74.000, 106.000), new Pose(90, 95)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            follower = Constants.createFollower(hardwareMap);
            route = new SequentialCommandGroup(
                    new FollowPath(follower, goesFromWallToShootPreload, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, linesUpToIntakeThirdRow, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, intakesThirdRow, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, shootsArtifacts, false, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, linesUpWithMiddleRow, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, intakesMiddleRow, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, goesToShootArtifacts, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, linesUpWithFurthestRow, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, intakesArtifacts, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, goesToShoot, true, 1)






            );
        }

        @Override
        public void start() {
            follower.setStartingPose(startPose);
            CommandScheduler.getInstance().schedule(route);
        }

        @Override
        public void loop() {
            CommandScheduler.getInstance().run();
            follower.update();
        }
    }
