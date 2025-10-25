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

    @Autonomous(name = "SmallRedAuto")
    public class SmallLaunchZoneAuto extends OpMode {
        Follower follower;
        SequentialCommandGroup route;
        @Override
        public void init() {
            CommandScheduler.getInstance().reset();
            follower = Constants.createFollower(hardwareMap);

            PathChain lineUpToIntake = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(83, 1), new Pose(83, 80), new Pose(104, 84)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            PathChain intakeFurthestRow = follower.pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(104, 84), new Pose(1, 84)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            PathChain goingToShoot = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(128, 84), new Pose(80, 80), new Pose(76, 12)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            PathChain lineUpToIntakeSecondRow = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(76, 12), new Pose(74, 63), new Pose(105, 60)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            PathChain intakeMiddleRow = follower.pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(105, 60), new Pose(129, 60)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            PathChain goToShoot = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(129, 60), new Pose(76, 58), new Pose(81, 14)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            PathChain lineUpToThirdRow = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(81, 14), new Pose(82, 40), new Pose(109, 36)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            PathChain intakeThirdRow = follower.pathBuilder()
                    .addPath(new BezierLine(new Pose(109, 36), new Pose(130, 36)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            PathChain lastTimeGoingToShoot = follower.pathBuilder()
                    .addPath(new BezierCurve(
                            new Pose(130, 36), new Pose(76, 32), new Pose(82, 10)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            route = new SequentialCommandGroup(
                    new FollowPath(follower, lineUpToIntake, true, 1)/*,
                    new FollowPath(follower, intakeFurthestRow, true, 1),
                    new FollowPath(follower, goingToShoot, true, 1),
                    new FollowPath(follower, lineUpToIntakeSecondRow, true, 1),
                    new FollowPath(follower, intakeMiddleRow, true, 1),
                    new FollowPath(follower, goToShoot, true, 1),
                    new FollowPath(follower, lineUpToThirdRow, true, 1),
                    new FollowPath(follower, intakeThirdRow),
                    new FollowPath(follower, lastTimeGoingToShoot, true, 1)*/
            );
        }

        @Override
        public void start() {
            CommandScheduler.getInstance().schedule(route);
            follower.setStartingPose(new Pose(83, 1, Math.toRadians(0)));
        }

        @Override
        public void loop() {
            CommandScheduler.getInstance().run();
            follower.update();
        }
    }
