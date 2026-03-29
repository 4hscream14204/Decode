package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.commands.LiftControlCommand;

@TeleOp(name = "Lift Distance Sensor Test")
public class LiftDistanceSensorTest extends OpMode {
    RobotBase robotBase;
    GamepadEx controller;

    @Override
    public void init() {
        robotBase = new RobotBase(hardwareMap);
        controller = new GamepadEx(gamepad1);
        //robotBase.liftSubsystem.setPosition(0.5);
        controller.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LiftControlCommand(robotBase)));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        telemetry.addData("Distance", robotBase.liftDistanceSensorSubsystem.getPosition());
        telemetry.update();
    }
}
