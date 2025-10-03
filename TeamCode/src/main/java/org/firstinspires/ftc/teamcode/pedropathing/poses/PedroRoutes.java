package org.firstinspires.ftc.teamcode.pedropathing.poses;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;

public class PedroRoutes {
    Follower follower;
    public void init() {
        follower = Constants.createFollower(hardwareMap);
    }
    //Pedro route shooting from small launch zone
    PathChain lineUpToIntake = follower.pathBuilder()
     .addPath(
      new BezierCurve(new Pose(82.517, 11.002), new Pose(64.719, 80.252), new Pose(104.000, 84.000)))
        .setTangentHeadingInterpolation()
    .build();

    PathChain intakeFurthestRow = follower.pathBuilder()
    .addPath(
      new BezierLine(new Pose(104.000, 84.000), new Pose(128.144, 84.000)))
            .setTangentHeadingInterpolation()
    .build();

    PathChain goingToShoot = follower.pathBuilder()
            .addPath(
                    new BezierCurve(new Pose(128.144, 84.000), new Pose(79.604, 79.604), new Pose(76.369, 12.297)))
            .setTangentHeadingInterpolation()
            .build();

    PathChain lineUpToIntakeSecondRow = follower.pathBuilder()
            .addPath(
                    new BezierCurve(
                            new Pose(76.369, 12.297), new Pose(74.427, 62.778), new Pose(104.845, 60.000)))
            .setTangentHeadingInterpolation()
            .build();

    PathChain IntakeMiddleRow = follower.pathBuilder()
            .addPath(
                    new BezierLine(new Pose(104.845, 60.000), new Pose(128.791, 60.000)))
            .setTangentHeadingInterpolation()
            .build();

    PathChain goToShoot = follower.pathBuilder()
            .addPath(
                    new BezierCurve(new Pose(128.791, 60.000), new Pose(76.369, 57.600), new Pose(80.575, 13.591)))
            .setTangentHeadingInterpolation()
            .build();

    PathChain lineUpToThirdRow = follower.pathBuilder()
            .addPath(
                    new BezierCurve(
                            new Pose(80.575, 13.591), new Pose(82.193, 40.449), new Pose(108.728, 36.243)))
            .setTangentHeadingInterpolation()
            .build();

    PathChain intakeThirdRow = follower.pathBuilder()
            .addPath(new BezierLine(new Pose(108.728, 36.243), new Pose(130.085, 36.000)))
            .setTangentHeadingInterpolation()
            .build();

    PathChain lastTimeGoingToShoot = follower.pathBuilder()
            .addPath(new BezierCurve(
                            new Pose(130.085, 36.000), new Pose(76.369, 32.360), new Pose(81.546, 9.708)))
            .setTangentHeadingInterpolation()
            .build();

//Pedro route shooting from big launch zone
    PathChain goesFromWallToShootPreload = follower.pathBuilder()
        .addPath(
                new BezierCurve(new Pose(79.000, 139.000), new Pose(73.000, 113.000), new Pose(100.000, 125.000)))
        .setTangentHeadingInterpolation()
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

}
