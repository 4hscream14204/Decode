package org.firstinspires.ftc.teamcode.opmode.auto;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;

@TeleOp(name = "Route in TeleOp")
public class RoutesInTeleopTest extends OpMode {
    GamepadEx chassis;
    Follower follower;
    Pose startPose = new Pose(56, 8, Math.toRadians(0));
    boolean automatedDrive;
    PathChain pathChain;
    double x;
    double y;
    SequentialCommandGroup route;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        chassis = new GamepadEx(gamepad1);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        pathChain = follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierCurve(follower::getPose, new Pose(42.1, 32.5, Math.toRadians(90)))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(9), 1))
                .build();

        chassis.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()-> follower.followPath(pathChain)), new InstantCommand(()->automatedDrive = true)));

        new Trigger(()->automatedDrive && (chassis.wasJustPressed(GamepadKeys.Button.B) || !follower.isBusy()))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->follower.startTeleopDrive(true)), new InstantCommand(()->automatedDrive = false)));

        route = new SequentialCommandGroup(
                new InstantCommand(()->new FollowPath(follower, pathChain, true, 1)),
                new WaitUntilCommand(()->follower.isBusy())
        );
    }

    @Override
    public void start() {
        follower.startTeleopDrive(true);
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        chassis.readButtons();
        follower.update();
        x = follower.getPose().getX();
        y = follower.getPose().getY();
        follower.setTeleOpDrive(-chassis.getLeftY(), -chassis.getLeftX(), -chassis.getRightX(), true);

        /*pathChain = follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(x, y, Math.toRadians(0)), new Pose(38.4, 33.4, Math.toRadians(90))))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90))
                .build();*/

        telemetry.addData("position", follower.getPose());
        telemetry.addData("velocity", follower.getVelocity());
        telemetry.addData("automatedDrive", automatedDrive);
    }
}
