package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

@TeleOp(name = "UNDERGLOW")
public class UNDERGLOW extends OpMode {
    Servo prism;
    GamepadEx chassis;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        prism = hardwareMap.get(Servo.class, "prism");
        chassis.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->prism.setPosition(0.225))));

        chassis.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->prism.setPosition(0))));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
    }
}
