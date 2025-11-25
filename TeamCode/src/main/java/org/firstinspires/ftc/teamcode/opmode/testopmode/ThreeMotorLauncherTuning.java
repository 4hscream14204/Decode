package org.firstinspires.ftc.teamcode.opmode.testopmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

@TeleOp(name = "Three Motor Launcher Tuning")
public class ThreeMotorLauncherTuning extends OpMode {
    GamepadEx main;
    RobotBase robotBase;
    Follower follower;
    boolean isFieldCentric;
    double velocity;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        main = new GamepadEx(gamepad1);
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.BLUEGOAL);

        main.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, 0)));

        main.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity += 10))));

        main.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity -= 10))));

        main.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity += 100))));

        main.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity -= 100))));

        new Trigger(()-> main.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .or(new Trigger(()-> main.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(main.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - main.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)))))
                .whenInactive (()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(0))));

        main.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->isFieldCentric = !isFieldCentric)
                ));
    }

    @Override
    public void start() {
        follower.setStartingPose(new Pose(88, 8, 0));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        main.readButtons();
        robotBase.limelightSubsystem.updateLimelight();
        follower.update();
        robotBase.chassisSubsystem.drive(main.getLeftY(), main.getLeftX(), main.getRightX(), robotBase.chassisSubsystem.bolSnapToTarget, isFieldCentric, robotBase.limelightSubsystem.getTargetX());
        telemetry.addData("Left", robotBase.launcherSubsystemLeft.getVelocity());
        telemetry.addData("Middle", robotBase.launcherSubsystemMiddle.getVelocity());
        telemetry.addData("Right", robotBase.launcherSubsystemRight.getVelocity());
        telemetry.addData("Limelight Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower));
        telemetry.update();
    }
}
