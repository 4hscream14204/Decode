package org.firstinspires.ftc.teamcode.opmode.Controllers;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commandgroups.general.Launch3ArtifactsDynamicCG;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchPatternCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.ToggleAlliance;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferGreenBallCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferPurpleBallCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferTwoPurpleCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LauncherControllerKeys {
    public void addLauncherDriver(GamepadEx launcherController, RobotBase robotBase, Follower follower){

        launcherController.getGamepadButton(GamepadKeys.Button.OPTIONS)
                        .whenPressed(()-> CommandScheduler.getInstance().schedule(new ToggleAlliance(robotBase)));

        launcherController.getGamepadButton(GamepadKeys.Button.CROSS)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new Launch3ArtifactsDynamicCG(robotBase, follower, robotBase.chassisSubsystem.isLockingToGate)));

        launcherController.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new TransferPurpleBallCommandGroup(robotBase)));

        launcherController.getGamepadButton(GamepadKeys.Button.SQUARE)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new TransferGreenBallCommandGroup(robotBase)));
        launcherController.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new TransferTwoPurpleCommandGroup(robotBase)));

        launcherController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new LaunchPatternCommandGroup(robotBase)));
    }
}
