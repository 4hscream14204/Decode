package org.firstinspires.ftc.teamcode.opmode.Controllers;

import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.ToggleAlliance;
import org.firstinspires.ftc.teamcode.commandgroups.general.ToggleLaunchZoneCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class BackupControllerKeys {

    public void addBackupController(GamepadEx backupController, RobotBase robotBase) {

        backupController.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new ToggleAlliance(robotBase)));

        backupController.getGamepadButton(GamepadKeys.Button.BACK)
                .whenPressed(()->CommandScheduler.getInstance().schedule(
                        new ToggleLaunchZoneCommandGroup(robotBase)));

        backupController.getGamepadButton(GamepadKeys.Button.SQUARE)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.CROSS)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)));

        backupController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, 1950)));


        backupController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, 2200)));

        backupController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),  new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME)))
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME)))
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE))));

        backupController.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.FAR))));
    }
}
