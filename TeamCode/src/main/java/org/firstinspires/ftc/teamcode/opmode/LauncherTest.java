package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

@Disabled
@TeleOp(name = "Launcher Test")
public class LauncherTest extends OpMode {
    RobotBase robotBase;
    GamepadEx chassis;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robotBase = new RobotBase(hardwareMap);
        chassis = new GamepadEx(gamepad1);

        chassis.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(0))));

        chassis.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(-0.25))));

        chassis.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(-0.5))));

        chassis.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(-0.75))));

        chassis.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(-1))));
    }

    @Override
    public void loop() {
        chassis.readButtons();
        CommandScheduler.getInstance().run();
        telemetry.addData("Motor Velocity", robotBase.launcherSubsystem.launcherMotorLeft.getVelocity());
    }
}
