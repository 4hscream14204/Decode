package org.firstinspires.ftc.teamcode.opmode.auto;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;


    @Autonomous(name = "Big Launch Zone Red Auto")
    public class BigLaunchZoneRedAuto extends OpMode {
        Follower follower;
        SequentialCommandGroup route;
        Pose startPose = new Pose(111.62, 135.55, Math.toRadians(0));
        Pose parkPose = new Pose(127, 95, Math.toRadians(0));
        Pose launchPose = new Pose(90, 95, Math.toRadians(0));
        Pose startToLaunchControl = new Pose(89.321, 136.355, Math.toRadians(0));
        Pose launchToTopRowControl = new Pose(79, 84, Math.toRadians(0));
        Pose preIntakeTopRow = new Pose(104, 84, Math.toRadians(0));
        Pose intakeTopRow = new Pose(126, 84, Math.toRadians(0));
        Pose topRowToLaunchControl = new Pose(90.9, 78.23, Math.toRadians(0));
        Pose launchToMiddleRow = new Pose(74.000, 62.000, Math.toRadians(0));
        Pose preIntakeMiddleRow = new Pose(103.000, 60, Math.toRadians(0));
        Pose intakeMiddleRow = new Pose(127.497, 60, Math.toRadians(0));
        Pose middleRowToLaunchControl = new Pose(79.604, 54.688, Math.toRadians(0));
        Pose launchToBottomRowControl = new Pose(77.016, 85.753, Math.toRadians(0));
        Pose preIntakeBottomRow = new Pose(104.178, 35, Math.toRadians(0));
        Pose intakeBottomRow = new Pose(127, 35, Math.toRadians(0));
        Pose bottomRowToLaunchControl = new Pose(99.020, 40.449);
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
        PathChain park;
        GamepadEx chassis;
        DesiredRows desiredRows = DesiredRows.THREE;
        SequentialCommandGroup routeMiddleRow;
        SequentialCommandGroup routeBottomRow;
        boolean middleRowDone = false;
        boolean bottomRowDone = false;
        int secondsToWait = 0;
        ElapsedTime timer;

        public enum DesiredRows{
            ONE,
            TWO,
            THREE
        }

        @Override
        public void init() {
            follower = Constants.createFollower(hardwareMap);
            CommandScheduler.getInstance().reset();
            chassis = new GamepadEx(gamepad1);
            timer = new ElapsedTime();

            chassis.getGamepadButton(GamepadKeys.Button.A)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->desiredRows = DesiredRows.THREE)));

            chassis.getGamepadButton(GamepadKeys.Button.B)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->desiredRows = DesiredRows.TWO)));

            chassis.getGamepadButton(GamepadKeys.Button.X)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->desiredRows = DesiredRows.ONE)));

            chassis.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->secondsToWait = secondsToWait + 1000)));

            chassis.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->secondsToWait = secondsToWait - 1000)));

            goesFromWallToShootPreload = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(startPose, startToLaunchControl, launchPose))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            //Closest line//
            linesUpToIntakeThirdRow = follower.pathBuilder()
                    .addPath(new BezierCurve(launchPose, launchToTopRowControl, preIntakeTopRow))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();
            intakesThirdRow = follower.pathBuilder()
                    .addPath(
                            new BezierLine(preIntakeTopRow, intakeTopRow))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();
            shootsArtifacts = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(intakeTopRow, topRowToLaunchControl, launchPose))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            //Middle line//
            linesUpWithMiddleRow = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(launchPose, launchToMiddleRow, preIntakeMiddleRow))
                    .setConstantHeadingInterpolation(0)
                    .build();
            intakesMiddleRow = follower.pathBuilder()
                    .addPath(
                            new BezierLine(preIntakeMiddleRow, intakeMiddleRow))
                    .setConstantHeadingInterpolation(0)
                    .build();
            goesToShootArtifacts = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(intakeMiddleRow, middleRowToLaunchControl, launchPose))
                    .setConstantHeadingInterpolation(0)
                    .build();

            //furthest line//
            linesUpWithFurthestRow = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(launchPose, launchToBottomRowControl, preIntakeBottomRow))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();
            intakesArtifacts = follower.pathBuilder()
                    .addPath(
                            new BezierLine(preIntakeBottomRow, intakeBottomRow))
                    .setConstantHeadingInterpolation(0)
                    .build();
            goesToShoot = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(intakeBottomRow, bottomRowToLaunchControl, launchPose))
                    .setConstantHeadingInterpolation(0)
                    .build();

            park = follower.pathBuilder()
                    .addPath(new BezierLine(launchPose, parkPose))
                    .setConstantHeadingInterpolation(0)
                    .build();

            follower = Constants.createFollower(hardwareMap);

            routeMiddleRow = new SequentialCommandGroup(
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, linesUpWithMiddleRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, intakesMiddleRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, goesToShootArtifacts, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->middleRowDone = true));

            routeBottomRow = new SequentialCommandGroup(
                new FollowPath(follower, linesUpWithFurthestRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, intakesArtifacts, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, goesToShoot, true, 1),
                new InstantCommand(()->bottomRowDone = true)
            );

            route = new SequentialCommandGroup(
                    new WaitUntilCommand(()->(secondsToWait) <= timer.milliseconds()),
                    new FollowPath(follower, goesFromWallToShootPreload, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new WaitCommand(1000),
                    new FollowPath(follower, linesUpToIntakeThirdRow, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new WaitCommand(1000),
                    new FollowPath(follower, intakesThirdRow, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new WaitCommand(1000),
                    new FollowPath(follower, shootsArtifacts, false, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new ConditionalCommand(new InstantCommand(()->routeMiddleRow.schedule()), new InstantCommand(()->follower.followPath(park)),()-> (desiredRows == DesiredRows.TWO || desiredRows == DesiredRows.THREE)),
                    new WaitUntilCommand(()-> middleRowDone),
                    new ConditionalCommand(new InstantCommand(()->routeBottomRow.schedule()), new InstantCommand(()->follower.followPath(park)), ()-> (desiredRows == DesiredRows.THREE)),
                    new WaitUntilCommand(()->bottomRowDone),
                    new FollowPath(follower, park, true, 1));
        }

        @Override
        public void init_loop(){
            CommandScheduler.getInstance().run();
            chassis.readButtons();
            telemetry.addData("Rows", desiredRows);
            telemetry.addData("Wait Time", secondsToWait);
        }

        @Override
        public void start() {
            follower.setStartingPose(startPose);
            CommandScheduler.getInstance().schedule(route);
            timer.reset();
        }

        @Override
        public void loop() {
            CommandScheduler.getInstance().run();
            follower.update();
            telemetry.addData("Rows", desiredRows);
            telemetry.addData("MS", secondsToWait);
        }
    }
