package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.bylazar.field.PanelsField;
import com.bylazar.gamepad.PanelsGamepad;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.commandgroups.general.ChangeHeadingLockCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.poses.PoseGenerator;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

@TeleOp(name = "Ball Pose Test")
public class BallPoseTest extends OpMode {
    GamepadEx chassis;
    RobotBase robotBase;
    Follower follower;
    PoseGenerator poseGenerator;
    Supplier<PathChain> pathChain;
    boolean automatedDrive;

    @Override
    public void init() {
        chassis = new GamepadEx(gamepad1);
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.PURPLEARTIFACT);
        poseGenerator = new PoseGenerator(robotBase.limelightSubsystem, follower);

        chassis.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new ChangeHeadingLockCommandGroup(robotBase)));

        pathChain = () -> follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(follower::getPose, poseGenerator.calculatePose())))
                .setHeadingInterpolation(HeadingInterpolator.constant((follower.getHeading() - Math.toRadians(robotBase.limelightSubsystem.getTargetX()))))
                .build();
    }

    @Override
    public void start() {
        follower.setPose(new Pose(0, 0, Math.toRadians(0)));
        follower.startTeleopDrive(true);
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        follower.update();
        robotBase.limelightSubsystem.updateLimelight();

        chassis.readButtons();

        Pose generatedPose = poseGenerator.calculatePose();

        follower.setTeleOpDrive(chassis.getLeftY(), -chassis.getLeftX(), -chassis.getRightX(), false);

        if (gamepad1.aWasPressed()) {
            follower.followPath(pathChain.get());
            automatedDrive = true;
        }
        //Stop automated following if the follower is done
        if (automatedDrive && (gamepad1.bWasPressed() || !follower.isBusy())) {
            follower.startTeleopDrive();
            automatedDrive = false;
        }

        robotBase.intakeSubsystem.intake(gamepad1.left_trigger - gamepad1.right_trigger);

        //robotBase.chassisSubsystem.drive(chassis.getLeftY(), chassis.getLeftX(), chassis.getRightX(), robotBase.chassisSubsystem.bolSnapToTarget, true, robotBase.limelightSubsystem.getTargetX());

        telemetry.addData("Robot heading", Math.toDegrees(follower.getHeading()));
        telemetry.addData("Bot Pose X", follower.getPose().getX());
        telemetry.addData("Bot Pose Y", follower.getPose().getY());
        telemetry.addData("Ball Pose X", generatedPose.getX());
        telemetry.addData("Ball Pose Y", generatedPose.getY());
        telemetry.addData("Target Z", robotBase.limelightSubsystem.getTargetZ());
        telemetry.addData("Target X", robotBase.limelightSubsystem.getTargetX());
    }
}
