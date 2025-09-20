package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Schematicannon TeleOp")
public class TeleOp extends OpMode{

    public RobotBase robotBase;
    public GamepadEx chassisController;

    @Override
    public void init(){
        CommandScheduler.getInstance().reset();
        robotBase = new RobotBase(hardwareMap);

        chassisController = new GamepadEx(gamepad1);

        chassisController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whileActiveContinuous(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()-> robotBase.intakeSubsystem.intake(-1))
                        .whenFinished(()->CommandScheduler.getInstance().schedule( new InstantCommand(()-> robotBase.intakeSubsystem.intake(0))))
                ));

    }

    public void loop(){
        chassisController.readButtons();

        CommandScheduler.getInstance().run();
    }
}
