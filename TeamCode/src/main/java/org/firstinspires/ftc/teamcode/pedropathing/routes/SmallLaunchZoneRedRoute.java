package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
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
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commandgroups.LaunchCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.TransferPatternCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

@Autonomous(name = "SmallRedAuto")
public class SmallLaunchZoneRedRoute extends OpMode {
    Follower follower;
    RobotBase robotBase;
    TransferPatternCommandGroup transferPatternCommandGroup;
    SequentialCommandGroup route;
    SequentialCommandGroup routeMiddleRow;
    SequentialCommandGroup routeTopRow;
    SequentialCommandGroup routePark;
    GamepadEx chassis;
    ElapsedTime timer;
    SmallLaunchZoneRedRoute.DesiredRows desiredRows = DesiredRows.THREE;
    boolean middleRowDone = false;
    boolean topRowDone = false;
    int secondsToWait = 0;

    public enum DesiredRows{
        ONE,
        TWO,
        THREE
    }

    @Override
    public void init() {

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

        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);

        PathChain lineUpToIntake = follower.pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(88.71, 16), new Pose(83, 80), new Pose(104, 84)))
                .setConstantHeadingInterpolation(0)
                .build();

        PathChain intakeFurthestRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(new Pose(104, 84), new Pose(126, 84)))
                .setConstantHeadingInterpolation(0)
                .build();

        PathChain goingToShoot = follower.pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(126, 84), new Pose(80, 80), new Pose(88.71, 16)))
                .setConstantHeadingInterpolation(0)
                .build();

        PathChain lineUpToIntakeSecondRow = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(88.71, 16), new Pose(74, 63), new Pose(105, 59)))
                .setConstantHeadingInterpolation(0)
                .build();

        PathChain intakeMiddleRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(new Pose(105, 59), new Pose(129, 59)))
                .setConstantHeadingInterpolation(0)
                .build();

        PathChain goToShoot = follower.pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(129, 59), new Pose(76, 58), new Pose(88.71, 16)))
                .setConstantHeadingInterpolation(0)
                .build();

        PathChain lineUpToThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(88.71, 16), new Pose(82, 40), new Pose(104, 36)))
                .setConstantHeadingInterpolation(0)
                .build();

        PathChain intakeThirdRow = follower.pathBuilder()
                .addPath(new BezierLine(new Pose(104, 36), new Pose(130, 36)))
                .setConstantHeadingInterpolation(0)
                .build();

        PathChain firstTimeGoingToShoot = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(130, 36), new Pose(76, 32), new Pose(88.71, 16)))
                .setConstantHeadingInterpolation(0)
                .build();

        PathChain park = follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(88.17, 16), new Pose(100, 19), new Pose(112, 8)))
                .setConstantHeadingInterpolation(0)
                .build();

        routeMiddleRow = new SequentialCommandGroup(
                new FollowPath(follower, lineUpToIntakeSecondRow, true, 1),
                new FollowPath(follower, intakeMiddleRow, true, 1),
                new FollowPath(follower, goToShoot, true, 1),
                new InstantCommand(()->middleRowDone = true)
        );

        routeTopRow = new SequentialCommandGroup(
                new FollowPath(follower, lineUpToIntake, true, 1),
                new FollowPath(follower, intakeFurthestRow, true, 1),
                new FollowPath(follower, goingToShoot, true, 1),
                new InstantCommand(()->topRowDone = true)
        );

        routePark = new SequentialCommandGroup(
                new FollowPath(follower, park, true, 1)
        );

        route = new SequentialCommandGroup(
                // new FollowPath(follower, lineUpToIntake, true, 1),
                new FollowPath(follower, lineUpToThirdRow, true, 1),
                new FollowPath(follower, intakeThirdRow, true, 1),
                new FollowPath(follower, firstTimeGoingToShoot, true, 1),
                new LaunchCommandGroup(robotBase),
                new InstantCommand(()->transferPatternCommandGroup.schedule()),
                new WaitUntilCommand(()->transferPatternCommandGroup.isFinished()),
                new ConditionalCommand(new InstantCommand(()->routeMiddleRow.schedule()), new InstantCommand(()->routePark.schedule()), ()->desiredRows == DesiredRows.TWO || desiredRows == DesiredRows.THREE),
                new WaitUntilCommand(()->middleRowDone),
                new ConditionalCommand(new InstantCommand(()->routeTopRow.schedule()), new InstantCommand(()->routePark.schedule()), ()->desiredRows == DesiredRows.THREE),
                new WaitUntilCommand(()->topRowDone),
                new InstantCommand(()->routePark.schedule())
        );
    }

    @Override
    public void init_loop() {
        chassis.readButtons();
        CommandScheduler.getInstance().run();
        telemetry.addData("Rows", desiredRows);
        telemetry.update();
    }

    @Override
    public void start() {
        follower.setStartingPose(new Pose(83, 1, Math.toRadians(0)));
        CommandScheduler.getInstance().schedule(route);
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        follower.update();
        telemetry.addData("Rows", desiredRows);
        telemetry.update();
    }
}
