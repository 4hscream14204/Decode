package org.firstinspires.ftc.teamcode.opmode.testopmode;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commandgroups.general.ChangeHeadingLockCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

@TeleOp(name = "Three Motor Launcher Tuning")
public class ThreeMotorLauncherTuning extends OpMode {
    GamepadEx main;
    RobotBase robotBase;
    Follower follower;
    boolean isFieldCentric;
    double velocity = 0;
    ElapsedTime timer;
    Pose redGoalPose = new Pose(132, 137);
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        main = new GamepadEx(gamepad1);
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.REDGOAL);
        timer = new ElapsedTime();

        main.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, 0)));

        main.getGamepadButton(GamepadKeys.Button.X)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE))));

        main.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.FAR))));

        main.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->velocity += 10)));

        main.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->velocity -= 10)));

        main.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->velocity += 100)));

        main.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->velocity -= 100)));

        main.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new ChangeHeadingLockCommandGroup(robotBase)
                ));

        main.getGamepadButton(GamepadKeys.Button.PS)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new Transfer3BallsNoCameraCommandGroup(robotBase)));

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
        timer.reset();
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        main.readButtons();
        robotBase.limelightSubsystem.updateLimelight();
        follower.update();
        robotBase.launcherSubsystemLeft.setVelocitySimple(velocity);
        robotBase.launcherSubsystemMiddle.setVelocitySimple(velocity);
        robotBase.launcherSubsystemRight.setVelocitySimple(velocity);
        robotBase.chassisSubsystem.drive(main.getLeftY(), main.getLeftX(), main.getRightX(), isFieldCentric, timer, robotBase.limelightSubsystem.getTargetX(), follower);
        telemetry.addData("Left", robotBase.launcherSubsystemLeft.getVelocity());
        telemetry.addData("Middle", robotBase.launcherSubsystemMiddle.getVelocity());
        telemetry.addData("Right", robotBase.launcherSubsystemRight.getVelocity());
        telemetry.addData("Target Velocity", velocity);
        telemetry.addData("Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower, redGoalPose));
        telemetry.update();
    }
}
