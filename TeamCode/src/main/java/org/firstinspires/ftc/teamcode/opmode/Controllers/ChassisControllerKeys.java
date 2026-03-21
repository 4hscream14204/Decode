package org.firstinspires.ftc.teamcode.opmode.Controllers;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commandgroups.general.ChangeHeadingLockCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.ToggleTiltCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class ChassisControllerKeys{
    public void addMainController(GamepadEx mainController, RobotBase robotBase){

        mainController.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(
                        new InstantCommand(() -> robotBase.chassisSubsystem.toggleFieldCentric())
                ));

        mainController.getGamepadButton(GamepadKeys.Button.SHARE)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new InstantCommand(() -> robotBase.chassisSubsystem.resetIMU())));

        mainController.getGamepadButton(GamepadKeys.Button.SQUARE)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        mainController.getGamepadButton(GamepadKeys.Button.CROSS)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        mainController.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        mainController.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(
                        new ChangeHeadingLockCommandGroup(robotBase)
                ));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .toggleWhenPressed(new InstantCommand(()->robotBase.chassisSubsystem.isLockingToGate = false), new InstantCommand(()->robotBase.chassisSubsystem.isLockingToGate = true));

        mainController.getGamepadButton(GamepadKeys.Button.PS)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new ToggleTiltCommandGroup(robotBase)));

        new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .or(new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)))))
                .whenInactive (()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(0))));
    }
}
