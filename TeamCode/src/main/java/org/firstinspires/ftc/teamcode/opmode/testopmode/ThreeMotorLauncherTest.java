package org.firstinspires.ftc.teamcode.opmode.testopmode;

import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

@TeleOp(name = "Three Motor Launcher Test")
public class ThreeMotorLauncherTest extends OpMode {
    GamepadEx main;
    RobotBase robotBase;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        main = new GamepadEx(gamepad1);
        robotBase = new RobotBase(hardwareMap);
        main.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystemMiddle.setVelocity(1700))));
        main.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystemLeft.setVelocity(1700))));
        main.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystemRight.setVelocity(1700))));

        new Trigger(()-> main.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .or(new Trigger(()-> main.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(main.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - main.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)))))
                .whenInactive (()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(0))));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        main.readButtons();
        telemetry.addData("Left", robotBase.launcherSubsystemLeft.getVelocity());
        telemetry.addData("Middle", robotBase.launcherSubsystemMiddle.getVelocity());
        telemetry.addData("Right", robotBase.launcherSubsystemRight.getVelocity());
        telemetry.addData("Intake", robotBase.intakeSubsystem.intakeMotor.getCurrentPosition());
        telemetry.update();
    }
}
