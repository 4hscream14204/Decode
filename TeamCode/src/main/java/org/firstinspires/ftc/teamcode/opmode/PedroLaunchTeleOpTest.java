package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.pedropathing.routes.BuildPath;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;

import java.util.function.Supplier;

@TeleOp(name = "Pedro Launch TeleOp")
public class PedroLaunchTeleOpTest extends OpMode {
    GamepadEx chassis;
    Follower follower;
    Pose startPose = new Pose(56, 8, Math.toRadians(90));
    boolean automatedDrive;
    //Supplier<PathChain> pathChain;
    PathChain pathChain;
    double x;
    double y;
    double heading;
    GoBildaPinpointDriver pinpoint;
    SequentialCommandGroup route;
    BuildPath pathHelper;
    @Override
    public void init() {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 56, 8, AngleUnit.RADIANS, Math.toRadians(90)));
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        CommandScheduler.getInstance().reset();
        chassis = new GamepadEx(gamepad1);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        pathHelper = new BuildPath(follower);

        /*pathChain =()-> follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierCurve(follower::getPose, new Pose(77, 99, Math.toRadians(-55)))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(35), 1))
                .build();*/
        pathChain = pathHelper.buildPath(follower.getPose(), new Pose(77, 99, Math.toRadians(-55)), follower.getHeading(), -55.0);


        chassis.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()-> follower.followPath(pathChain)), new InstantCommand(()->automatedDrive = true)));

        new Trigger(()->automatedDrive && (chassis.wasJustPressed(GamepadKeys.Button.B) || !follower.isBusy()))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->follower.startTeleopDrive(true)), new InstantCommand(()->automatedDrive = false)));

        /*route = new SequentialCommandGroup(
                new InstantCommand(()->new FollowPath(follower, pathChain, true, 1)),
                new WaitUntilCommand(()->follower.isBusy())
        );*/
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
        //pinpoint.setPosX(follower.getPose().getX(), DistanceUnit.INCH);
        //pinpoint.setPosY(follower.getPose().getY(), DistanceUnit.INCH);
        x = follower.getPose().getX();
        y = follower.getPose().getY();
        heading = follower.getHeading();
        follower.setTeleOpDrive(chassis.getLeftY(), -chassis.getLeftX(), -chassis.getRightX(), true);

        /*pathChain = follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(x, y, Math.toRadians(0)), new Pose(38.4, 33.4, Math.toRadians(90))))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90))
                .build();*/

        telemetry.addData("position", follower.getPose());
        telemetry.addData("velocity", follower.getVelocity());
        telemetry.addData("Heading: ", Math.toDegrees(follower.getHeading()));
        telemetry.addData("Pinpoint X", pinpoint.getPosition().getX(DistanceUnit.INCH));
        telemetry.addData("Pinpoint Y", pinpoint.getPosition().getY(DistanceUnit.INCH));
        telemetry.addData("automatedDrive", automatedDrive);
    }
}
