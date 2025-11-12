package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.commandgroups.general.Launch3ArtifactsNoSortingCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchPatternCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.RGBLightLeftColorCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.RGBLightMiddleColorCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.RGBLightRightColorCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferGreenBallCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.CameraLight;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

@Disabled
@TeleOp(name = ("V1 Teleop"))
    public class V1TeleOp extends OpMode {
        RobotBase robotBase;
        GamepadEx armController;
        GamepadEx chassisController;
        RGBLightLeftColorCommandGroup rgbLightLeftColorCommandGroup;
        RGBLightMiddleColorCommandGroup rgbLightMiddleColorCommandGroup;
        RGBLightRightColorCommandGroup rgbLightRightColorCommandGroup;
        boolean isFieldCentric = true;
        boolean bolSnapToTarget = false;
        //Follower follower;
        @Override
        public void init() {
            CommandScheduler.getInstance().reset();
            robotBase = new RobotBase(hardwareMap);
            //follower = Constants.createFollower(hardwareMap);
            robotBase.sorterCameraSubsystem.getAnalysis();
            robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.BLUEGOAL);
            robotBase.limelightSubsystem.changePipeline(Limelight.limelightPipelines.BLUEGOAL);
            robotBase.chassisSubsystem.pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 56, 8, AngleUnit.RADIANS, Math.toRadians(0)));

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
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchPatternCommandGroup(robotBase)));

            chassisController.getGamepadButton(GamepadKeys.Button.X)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferGreenBallCommandGroup(robotBase)));
            chassisController.getGamepadButton(GamepadKeys.Button.Y)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new Launch3ArtifactsNoSortingCommandGroup(robotBase)));

            chassisController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                            .whenPressed(()->CommandScheduler.getInstance().schedule(
                                    new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(robotBase.launcherSubsystem.getVelocity() + 10))
                            ));

            chassisController.getGamepadButton(GamepadKeys.Button.START)
                            .whenPressed(()->CommandScheduler.getInstance().schedule(
                                    new InstantCommand(()->isFieldCentric = !isFieldCentric)
                            ));

            chassisController.getGamepadButton(GamepadKeys.Button.BACK)
                            .whenPressed(()-> CommandScheduler.getInstance().schedule(
                                    new InstantCommand(()->bolSnapToTarget = !bolSnapToTarget)
                            ));

            chassisController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(
                            new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(robotBase.launcherSubsystem.getVelocity() - 10))
                    ));

            chassisController.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                            .whenPressed(()->CommandScheduler.getInstance().schedule(
                                    new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(robotBase.launcherSubsystem.getVelocity() + 100))
                            ));

            chassisController.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(
                            new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(robotBase.launcherSubsystem.getVelocity() - 100))
                    ));

            chassisController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new Transfer3BallsNoCameraCommandGroup(robotBase)));

            chassisController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(-18.5)))));

            /*mainController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.cameraLightSubsystemLeft.setShade(CameraLight.Shades.TESTLEFT))));


            mainController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                    .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.cameraLightSubsystemRight.setShade(CameraLight.Shades.TESTLEFT))));
*/
            /*new Trigger(()->robotBase.sorterCameraSubsystem.hasThreeArtifacts())
                    .whenActive(()->CommandScheduler.getInstance().schedule(new PreloadThreeArtifactsCommandGroup(robotBase)));*/

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                    .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                    .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                    .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.RED))));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.RED))));

            new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.RED))));
        }

        public void start(){
            CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.cameraLightSubsystemLeft.setShade(CameraLight.Shades.FULL)));
            CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.cameraLightSubsystemRight.setShade(CameraLight.Shades.FULL)));
            //follower.setTeleOpDrive(mainController.getLeftY(), mainController.getLeftX(), mainController.getRightX(), false);
            //follower.startTeleopDrive(true);
        }

     @Override
     public void loop() {
        CommandScheduler.getInstance().run();
        chassisController.readButtons();
        robotBase.sorterCameraSubsystem.getAnalysis();
        robotBase.chassisSubsystem.pinpoint.update();
        robotBase.limelightSubsystem.updateLimelight();
        robotBase.chassisSubsystem.drive(chassisController.getLeftY(), chassisController.getLeftX(), chassisController.getRightX(), bolSnapToTarget, isFieldCentric, robotBase.limelightSubsystem.getTargetX());
        telemetry.addData("Launcher Velocity", robotBase.launcherSubsystem.getVelocity());
        telemetry.addData("Intake Power", robotBase.intakeSubsystem.intakeMotor.getPower());
        telemetry.addData("Left Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchLeft());
        telemetry.addData("Middle Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchMiddle());
        telemetry.addData("Right Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchRight());
        telemetry.addData("Distance", robotBase.limelightSubsystem.getHorizontalDistance(-18.5));
        telemetry.addData("Heading", robotBase.chassisSubsystem.pinpoint.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Is Field Centric", robotBase.chassisSubsystem.isFieldCentric);
        telemetry.addData("TX", robotBase.limelightSubsystem.getTargetX());
        telemetry.update();
     }
 }