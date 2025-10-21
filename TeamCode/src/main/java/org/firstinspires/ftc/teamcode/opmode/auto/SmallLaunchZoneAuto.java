package org.firstinspires.ftc.teamcode.opmode.auto;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;

public class SmallLaunchZoneAuto {
    @Autonomous(name = "SmallRedAuto")
    public class SmallLaunchZone extends OpMode {
        Follower follower;
        SequentialCommandGroup route;
        @Override
        public void init() {
            CommandScheduler.getInstance().reset();
            follower = Constants.createFollower(hardwareMap);

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

            PathChain intakeMiddleRow = follower.pathBuilder()
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

            route = new SequentialCommandGroup(
                    new FollowPath(follower, lineUpToIntake, true, 1),
                    new FollowPath(follower, intakeFurthestRow, true, 1),
                    new FollowPath(follower, goingToShoot, true, 1),
                    new FollowPath(follower, lineUpToIntakeSecondRow, true, 1),
                    new FollowPath(follower, intakeMiddleRow, true, 1),
                    new FollowPath(follower, goToShoot, true, 1),
                    new FollowPath(follower, lineUpToThirdRow, true, 1),
                    new FollowPath(follower, intakeThirdRow),
                    new FollowPath(follower, lastTimeGoingToShoot, true, 1)
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

}
