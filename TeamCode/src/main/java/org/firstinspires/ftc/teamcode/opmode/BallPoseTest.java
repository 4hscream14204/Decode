package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.bylazar.field.PanelsField;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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

    @Override
    public void init() {
        chassis = new GamepadEx(gamepad1);
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.PURPLEARTIFACT);
        poseGenerator = new PoseGenerator(robotBase.limelightSubsystem, follower);

        chassis.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new ChangeHeadingLockCommandGroup(robotBase)));
    }

    @Override
    public void start() {
        follower.setPose(new Pose(0, 0, Math.toRadians(0)));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        follower.update();
        robotBase.limelightSubsystem.updateLimelight();

        Pose generatedPose = poseGenerator.calculatePose();

        robotBase.chassisSubsystem.drive(chassis.getLeftY(), chassis.getLeftX(), chassis.getRightX(), robotBase.chassisSubsystem.bolSnapToTarget, true, robotBase.limelightSubsystem.getTargetX());

        telemetry.addData("Robot heading", Math.toDegrees(follower.getHeading()));
        telemetry.addData("Bot Pose X", follower.getPose().getX());
        telemetry.addData("Bot Pose Y", follower.getPose().getY());
        telemetry.addData("Ball Pose X", generatedPose.getX());
        telemetry.addData("Ball Pose Y", generatedPose.getY());
        telemetry.addData("Target Z", robotBase.limelightSubsystem.getTargetZ());
        telemetry.addData("Target X", robotBase.limelightSubsystem.getTargetX());
    }
}
