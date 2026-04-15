package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.commands.TransferCommand;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;

import java.util.List;

@TeleOp(name = "Launcher PID Test")
public class LauncherPIDTest extends OpMode {
    VoltageSensor controlHubVoltageSensor;
    RobotBase robotBase;
    GamepadEx mainController;
    double power = 0;
    double velocity;
    Follower follower;
    @Override
    public void init() {
        robotBase = new RobotBase(hardwareMap);
        List <VoltageSensor> voltageSensors = hardwareMap.getAll(VoltageSensor.class);
        controlHubVoltageSensor = voltageSensors.get(0);
        mainController = new GamepadEx(gamepad1);
        follower = Constants.createFollower(hardwareMap);

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->velocity += 100)));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->velocity -= 100)));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->velocity += 10)));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->velocity -= 10)));

        mainController.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(0))));

        mainController.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferCommand(robotBase)));

        new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .or(new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)))))
                .whenInactive (()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(0))));


    }

    @Override
    public void start() {
        robotBase.chassisSubsystem.pinpoint.setHeading(0, AngleUnit.DEGREES);
        robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK);
        robotBase.turretSubsystem.setPositionDeg(90);
        follower.setStartingPose(new Pose(92, 10, 0));
    }

    @Override
    public void loop() {
        mainController.readButtons();
        //robotBase.chassisSubsystem.pinpoint.update();
        follower.update();
        robotBase.chassisSubsystem.drive(mainController.getLeftY(), mainController.getLeftX(), mainController.getRightX(), true);
        CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(velocity)));

        telemetry.addData("Distance", follower.getPose().distanceFrom(new Pose(144, 138)));
        telemetry.addData("Voltage: ", controlHubVoltageSensor.getVoltage());
        telemetry.addData("Velocity Variable: ", velocity);
        telemetry.addData("Velocity: ", robotBase.launcherSubsystem.launcherMotor.getVelocity());
        CommandScheduler.getInstance().run();
    }
}
