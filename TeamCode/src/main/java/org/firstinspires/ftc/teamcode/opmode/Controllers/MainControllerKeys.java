package org.firstinspires.ftc.teamcode.opmode.Controllers;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commandgroups.general.ChangeHeadingLockCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.Launch3ArtifactsDynamicCG;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchPatternCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.ToggleTiltCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferGreenBallCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferPurpleBallCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferTwoPurpleCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Hood;

public class MainControllerKeys {

    public void addMainController(GamepadEx mainController, RobotBase robotBase, Follower follower) {

         mainController.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(
                        new InstantCommand(() -> robotBase.chassisSubsystem.toggleFieldCentric())
                ));

        mainController.getGamepadButton(GamepadKeys.Button.SHARE)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new InstantCommand(() -> robotBase.chassisSubsystem.resetIMU())));

        mainController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(
                        new ChangeHeadingLockCommandGroup(robotBase)
                ));

        mainController.getGamepadButton(GamepadKeys.Button.CROSS)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new Launch3ArtifactsDynamicCG(robotBase, follower)));

        mainController.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new TransferPurpleBallCommandGroup(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.SQUARE)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new TransferGreenBallCommandGroup(robotBase)));
        mainController.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new TransferTwoPurpleCommandGroup(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new LaunchPatternCommandGroup(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.FAR))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.chassisSubsystem.setTargetHeading(54))));

        mainController.getGamepadButton(GamepadKeys.Button.PS)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new ToggleTiltCommandGroup(robotBase)));

        new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .or(new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)))))
                .whenInactive (()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(0))));
    }
}
