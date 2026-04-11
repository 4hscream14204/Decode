package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.base.RobotBase;

import java.util.List;

@TeleOp(name = "Launcher PID Test")
public class LauncherPIDTest extends OpMode {
    VoltageSensor controlHubVoltageSensor;
    RobotBase robotBase;
    GamepadEx mainController;
    double power = 0;
    @Override
    public void init() {
        robotBase = new RobotBase(hardwareMap);
        List <VoltageSensor> voltageSensors = hardwareMap.getAll(VoltageSensor.class);
        controlHubVoltageSensor = voltageSensors.get(0);
        mainController = new GamepadEx(gamepad1);

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->power += 0.1)));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->power -= 0.1)));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->power += 0.01)));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->power -= 0.01)));

        mainController.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(power))));

        mainController.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(0))));


    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        mainController.readButtons();

        telemetry.addData("Voltage: ", controlHubVoltageSensor.getVoltage());
        telemetry.addData("Power Variable: ", power);
        telemetry.addData("Velocity: ", robotBase.launcherSubsystem.launcherMotor.getVelocity());
    }
}
