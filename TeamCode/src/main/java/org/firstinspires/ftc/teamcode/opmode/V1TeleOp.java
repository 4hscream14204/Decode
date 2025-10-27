package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commandgroups.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

@TeleOp(name = ("V1 Teleop"))
    public class V1TeleOp extends OpMode {
        public RobotBase robotBase;
        public GamepadEx armController;
        public GamepadEx chassisController;



        @Override
        public void init() {
            CommandScheduler.getInstance().reset();
            robotBase = new RobotBase(hardwareMap);

            robotBase.intakeSubsystem.intakeMotor.setPower(0);
            robotBase.launcherSubsystem.launcherMotor.setPower(0);
            robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.GREEN);
            robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE);

            chassisController = new GamepadEx(gamepad1);
            armController = new GamepadEx(gamepad2);

            new Trigger(()->chassisController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                    .or(new Trigger(()->chassisController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                    .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                            new InstantCommand(()->robotBase.intakeSubsystem.intake(gamepad1.right_trigger - gamepad1.left_trigger))))
                    .whenInactive (()->CommandScheduler.getInstance().schedule(
                            new InstantCommand(()->robotBase.intakeSubsystem.intake(0))));

            chassisController.getGamepadButton(GamepadKeys.Button.B)
                    .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1700))
                            .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))
                            ))));

            chassisController.getGamepadButton(GamepadKeys.Button.X)
                    .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(2000))
                            .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))
                            ))));
            chassisController.getGamepadButton(GamepadKeys.Button.Y)
                    .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(2500))
                            .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))
                            ))));

            chassisController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new Transfer3BallsNoCameraCommandGroup(robotBase)));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.GREEN)));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.GREEN)));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.GREEN)));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                    .whenActive(()->new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE)));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                    .whenActive(()->new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE)));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                    .whenActive(()->new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE)));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.RED)));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.RED)));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.RED)));

        }

     @Override
     public void loop() {
        CommandScheduler.getInstance().run();
        chassisController.readButtons();
        robotBase.chassisSubsystem.pinpoint.getPosition();
        robotBase.chassisSubsystem.drive(chassisController.getLeftY(), chassisController.getLeftX(), chassisController.getRightX(), false, true, robotBase.limelightSubsystem.getTargetY());
        telemetry.addData("Launcher Velocity", robotBase.launcherSubsystem.getVelocity());
        telemetry.addData("Intake Power", robotBase.intakeSubsystem.intakeMotor.getPower());
     }
 }