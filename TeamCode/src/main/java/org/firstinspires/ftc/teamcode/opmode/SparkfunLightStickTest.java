package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.hardware.sparkfun.SparkFunLEDStick;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "SparkfunLightStickTest")
public class SparkfunLightStickTest extends OpMode {

    SparkFunLEDStick ledStick;
    GamepadEx chassis;
    int value = -1000;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        chassis = new GamepadEx(gamepad1);
        ledStick = hardwareMap.get(SparkFunLEDStick.class, "ledStick");
        ledStick.turnAllOff();
        ledStick.setBrightness(50);
        chassis.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->ledStick.setColor(value))));
        chassis.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->value = value + 100)));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        chassis.readButtons();
        telemetry.addData("Value", value);
        //ledStick.setShade(0, 0);
    }
}
