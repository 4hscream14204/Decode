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
        GamepadEx chassis;
        DesiredRows desiredRows = DesiredRows.THREE;
        SequentialCommandGroup routeMiddleRow;
        SequentialCommandGroup routeBottomRow;
        int pathState;
        boolean middleRowDone = false;

        public enum DesiredRows{
            ONE,
            TWO,
            THREE
        }

        @Override
        public void init() {
            follower = Constants.createFollower(hardwareMap);
            startPose = new Pose(111.62, 135.55, Math.toRadians(0));
            CommandScheduler.getInstance().reset();
            chassis = new GamepadEx(gamepad1);

            chassis.getGamepadButton(GamepadKeys.Button.A)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->desiredRows = DesiredRows.THREE)));

            chassis.getGamepadButton(GamepadKeys.Button.B)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->desiredRows = DesiredRows.TWO)));

            chassis.getGamepadButton(GamepadKeys.Button.X)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->desiredRows = DesiredRows.ONE)));
            goesFromWallToShootPreload = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(startPose, new Pose(85.000, 115.000), new Pose(90, 95)))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            //Closest line//
            linesUpToIntakeThirdRow = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(102.903, 123.613), new Pose(79.000, 84.000), new Pose(104.000, 84.00)))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();
            intakesThirdRow = follower.pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(104.000, 84.000), new Pose(126, 84)))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();
            shootsArtifacts = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(126, 84), new Pose(90.9, 78.23), new Pose(90, 95)))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            //Middle line//
            linesUpWithMiddleRow = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(90, 95), new Pose(74.000, 62.000), new Pose(103.000, 60)))
                    .setConstantHeadingInterpolation(0)
                    .build();
            intakesMiddleRow = follower.pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(103.000, 60), new Pose(127.497, 60)))
                    .setConstantHeadingInterpolation(0)
                    .build();
            goesToShootArtifacts = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(127.497, 60), new Pose(79.604, 54.688), new Pose(90, 95)))
                    .setConstantHeadingInterpolation(0)
                    .build();

            //furthest line//
            linesUpWithFurthestRow = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(90, 95), new Pose(77.016, 85.753), new Pose(104.178, 35)))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();
            intakesArtifacts = follower.pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(104.178, 35), new Pose(127, 35)))
                    .setConstantHeadingInterpolation(0)
                    .build();
            goesToShoot = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(new Pose(127, 35), new Pose(99.020, 40.449), new Pose(90, 95)))
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
                new FollowPath(follower, goesToShoot, true, 1)
            );

            route = new SequentialCommandGroup(
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
                    new ConditionalCommand(new InstantCommand(()->routeMiddleRow.schedule()), new InstantCommand(()->follower.turnToDegrees(90)),()-> (desiredRows == DesiredRows.TWO || desiredRows == DesiredRows.THREE)),
                    new WaitUntilCommand(()-> middleRowDone),
                    new ConditionalCommand(new InstantCommand(()->routeBottomRow.schedule()), new InstantCommand(()->follower.turnToDegrees(90)), ()-> (desiredRows == DesiredRows.THREE)));


                    /*new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, linesUpWithFurthestRow, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, intakesArtifacts, true, 1),
                    new WaitUntilCommand(()->!follower.isBusy()),
                    new FollowPath(follower, goesToShoot, true, 1)*/
        }

        private void setPathState(int m_pathState){
            pathState = m_pathState;
        }

        @Override
        public void init_loop(){
            CommandScheduler.getInstance().run();
            chassis.readButtons();
            telemetry.addData("Rows", desiredRows);
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
            telemetry.addData("Rows", desiredRows);
            telemetry.addData("Path", follower.getCurrentPathChain());
        }
    }
