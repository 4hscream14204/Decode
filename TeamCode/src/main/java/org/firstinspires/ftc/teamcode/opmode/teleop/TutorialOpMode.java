package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.RobotBase;

@TeleOp(name = "Tutorial")
public class TutorialOpMode extends OpMode {
    RobotBase robotBase;
    GamepadEx player1;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robotBase = new RobotBase(hardwareMap);
        player1 = new GamepadEx(gamepad1);

        player1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1000))));

        player1.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(2000))));

        player1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1500))));

        player1.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))));
    }

    @Override
    public void loop() {
        player1.readButtons();
        CommandScheduler.getInstance().run();
        if(gamepad1.a){
            robotBase.launcherSubsystem.setVelocity(1000);
        }
        if(gamepad1.b){
            robotBase.launcherSubsystem.setVelocity(2000);
        }
        if(gamepad1.x){
            robotBase.launcherSubsystem.setVelocity(1500);
        }
        if(gamepad1.y){
            robotBase.launcherSubsystem.setVelocity(0);
        }
    }
}
