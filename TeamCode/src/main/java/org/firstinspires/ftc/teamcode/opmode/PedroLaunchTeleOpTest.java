package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

import java.util.function.Supplier;

@TeleOp(name = "Pedro Launch TeleOp")
public class PedroLaunchTeleOpTest extends OpMode {
    GamepadEx chassis;
    //Follower follower;
    RobotBase robotBase;
    Pose startPose = new Pose(56, 8, Math.toRadians(90));
    boolean automatedDrive;
    Supplier<PathChain> pathChain;
    PathChain pathChain1;
    double x;
    double y;
    double heading;
    GoBildaPinpointDriver pinpoint;
    SequentialCommandGroup route;
    //PathHelper pathHelper;
    DcMotorEx launchMotor;
    double velocity = 0;
    @Override
    public void init() {
        robotBase = new RobotBase(hardwareMap);
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 56, 8, AngleUnit.RADIANS, Math.toRadians(90)));
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        CommandScheduler.getInstance().reset();
        chassis = new GamepadEx(gamepad1);
        robotBase.limelightSubsystem.initLimelight();
        /*follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        pathHelper = new PathHelper(follower);*/

        /*pathChain =()-> follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierCurve(follower::getPose, new Pose(77, 99, Math.toRadians(-55)))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(35), 1))
                .build();*/
        //pathChain = pathHelper.buildPath(pathChain, follower.getPose(), new Pose(77, 99, Math.toRadians(-55)), -55.0);

        chassis.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new InstantCommand(()-> robotBase.launcherSubsystem.launcherMotor.setVelocity(robotBase.launcherSubsystem.launcherMotor.getVelocity() + 10))
                ));

        chassis.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new InstantCommand(()-> robotBase.launcherSubsystem.launcherMotor.setVelocity(robotBase.launcherSubsystem.launcherMotor.getVelocity() - 10))
                ));

        /*chassis.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()-> follower.followPath(pathChain.get())), new InstantCommand(()->automatedDrive = true)));

        new Trigger(()->automatedDrive && (chassis.wasJustPressed(GamepadKeys.Button.B) || !follower.isBusy()))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->follower.startTeleopDrive(true)), new InstantCommand(()->automatedDrive = false)));*/

        /*route = new SequentialCommandGroup(
                new InstantCommand(()->new FollowPath(follower, pathChain, true, 1)),
                new WaitUntilCommand(()->follower.isBusy())
        );*/
    }

    @Override
    public void start() {
        //follower.startTeleopDrive(true);
        robotBase.limelightSubsystem.changePipeline(Limelight.limelightPipelines.BLUEGOAL);
    }

    @Override
    public void loop() {
        robotBase.limelightSubsystem.updateLimelight();
        CommandScheduler.getInstance().run();
        chassis.readButtons();
        robotBase.launcherSubsystem.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(0));
        //follower.update();
        //pinpoint.setPosX(follower.getPose().getX(), DistanceUnit.INCH);
        //pinpoint.setPosY(follower.getPose().getY(), DistanceUnit.INCH);
      //  x = follower.getPose().getX();
     //   y = follower.getPose().getY();
     //   heading = follower.getHeading();
     //   follower.setTeleOpDrive(chassis.getLeftY(), -chassis.getLeftX(), -chassis.getRightX(), true);

        /*pathChain = follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(x, y, Math.toRadians(0)), new Pose(38.4, 33.4, Math.toRadians(90))))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90))
                .build();*/

        //telemetry.addData("position", follower.getPose());
        //telemetry.addData("velocity", follower.getVelocity());
        //telemetry.addData("Robot Heading: ", Math.toDegrees(follower.getHeading()));
        telemetry.addData("Pinpoint X", pinpoint.getPosition().getX(DistanceUnit.INCH));
        telemetry.addData("Pinpoint Y", pinpoint.getPosition().getY(DistanceUnit.INCH));
        telemetry.addData("automatedDrive", automatedDrive);
        telemetry.addData("Velocity", robotBase.launcherSubsystem.launcherMotor.getVelocity());
        telemetry.addData("Distance", robotBase.limelightSubsystem.getHorizontalDistance(0));
    }
}
