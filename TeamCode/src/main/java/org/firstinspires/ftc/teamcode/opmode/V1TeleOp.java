package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commandgroups.Launch3ArtifactsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.PreloadThreeArtifactsCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.RGBLightLeftColorCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.RGBLightMiddleColorCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.RGBLightRightColorCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

@TeleOp(name = ("V1 Teleop"))
    public class V1TeleOp extends OpMode {
        RobotBase robotBase;
        GamepadEx armController;
        GamepadEx chassisController;
        RGBLightLeftColorCommandGroup rgbLightLeftColorCommandGroup;
        RGBLightMiddleColorCommandGroup rgbLightMiddleColorCommandGroup;
        RGBLightRightColorCommandGroup rgbLightRightColorCommandGroup;
        @Override
        public void init() {
            CommandScheduler.getInstance().reset();
            robotBase = new RobotBase(hardwareMap);
            robotBase.sorterCameraSubsystem.getAnalysis();
            robotBase.limelightSubsystem.initLimelight();

            chassisController = new GamepadEx(gamepad1);
            armController = new GamepadEx(gamepad2);

            new Trigger(()->chassisController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                    .or(new Trigger(()->chassisController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                    .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                            new InstantCommand(()->robotBase.intakeSubsystem.intake(gamepad1.left_trigger - gamepad1.right_trigger))))
                    .whenInactive (()->CommandScheduler.getInstance().schedule(
                            new InstantCommand(()->robotBase.intakeSubsystem.intake(0))));

            chassisController.getGamepadButton(GamepadKeys.Button.A)
                            .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))));

            chassisController.getGamepadButton(GamepadKeys.Button.B)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1700))));

            chassisController.getGamepadButton(GamepadKeys.Button.X)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(2000))));
            chassisController.getGamepadButton(GamepadKeys.Button.Y)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new Launch3ArtifactsNoCameraCommandGroup(robotBase)));

            chassisController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new Transfer3BallsNoCameraCommandGroup(robotBase)));

            chassisController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)));

            new Trigger(()->robotBase.sorterCameraSubsystem.hasThreeArtifacts())
                    .whenActive(()->CommandScheduler.getInstance().schedule(new PreloadThreeArtifactsCommandGroup(robotBase)));

        }

     @Override
     public void loop() {
        CommandScheduler.getInstance().run();
        chassisController.readButtons();
        robotBase.sorterCameraSubsystem.getAnalysis();
        robotBase.chassisSubsystem.pinpoint.getPosition();
        robotBase.limelightSubsystem.updateLimelight();
        rgbLightLeftColorCommandGroup.schedule();
        rgbLightMiddleColorCommandGroup.schedule();
        rgbLightRightColorCommandGroup.schedule();
        robotBase.chassisSubsystem.drive(-chassisController.getLeftY(), -chassisController.getLeftX(), chassisController.getRightX(), false, true, robotBase.limelightSubsystem.getTargetY());
        telemetry.addData("Launcher Velocity", robotBase.launcherSubsystem.getVelocity());
        telemetry.addData("Intake Power", robotBase.intakeSubsystem.intakeMotor.getPower());
     }
 }