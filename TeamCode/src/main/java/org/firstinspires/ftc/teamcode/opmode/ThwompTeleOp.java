package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.ftc.PoseConverter;
import com.pedropathing.geometry.PedroCoordinates;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.commandgroups.general.ChangeHeadingLockCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.Launch3ArtifactsNoSortingCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchOneGreen;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchOnePurple;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchPatternCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchTwoPurple;
import org.firstinspires.ftc.teamcode.commandgroups.general.ToggleAlliance;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.CameraLight;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

@TeleOp(name = "Thwomp TeleOp")
public class ThwompTeleOp extends OpMode {
    RobotBase robotBase;
    GamepadEx backupController;
    GamepadEx mainController;
    boolean isFieldCentric = true;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robotBase = new RobotBase(hardwareMap);
        robotBase.sorterCameraSubsystem.getAnalysis();
        if(DataStorage.alliance == DecodeEnums.Alliance.BLUE){
            robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.BLUEGOAL);
        }
        else{
            robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.REDGOAL);
        }

        robotBase.chassisSubsystem.pinpoint.setPosition(PoseConverter.poseToPose2D(DataStorage.endPosition, PedroCoordinates.INSTANCE));

        robotBase.cameraLightSubsystemLeft.setShade(CameraLight.Shades.FULL);
        robotBase.cameraLightSubsystemRight.setShade(CameraLight.Shades.FULL);

        mainController = new GamepadEx(gamepad1);
        backupController = new GamepadEx(gamepad2);

        //Main Driver keybinds

        mainController.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->isFieldCentric = !isFieldCentric)
                ));

        mainController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new ChangeHeadingLockCommandGroup(robotBase)
                ));

        mainController.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new Launch3ArtifactsNoSortingCommandGroup(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchOnePurple(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchOneGreen(robotBase)));
        mainController.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchTwoPurple(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchPatternCommandGroup(robotBase)));

        new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .or(new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)))))
                .whenInactive (()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(0))));

        //Backup Driver Keybinds

        backupController.getGamepadButton(GamepadKeys.Button.START)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new ToggleAlliance(robotBase)));

        backupController.getGamepadButton(GamepadKeys.Button.BACK)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.chassisSubsystem.resetIMU())));

        backupController.getGamepadButton(GamepadKeys.Button.X)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.A)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)));
/*
        backupController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(750))
                ));

        backupController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1700))))
                .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))));


        backupController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(2000))))
                .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))));

        backupController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new InstantCommand(()-> robotBase.launcherSubsystem.setVelocity(1500))
                ));
*/
        //Custom Triggers for Sorter and such

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

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorLeftSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorMiddleSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorRightSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));
    }

    @Override
    public void init_loop(){
        telemetry.addData("Is Running", robotBase.limelightSubsystem.limelight.isRunning());
        telemetry.addData("Is connected: ", robotBase.limelightSubsystem.limelight.isConnected());
        telemetry.addData("Hue L", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.LEFT));
        telemetry.addData("Hue M", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.MIDDLE));
        telemetry.addData("Hue R", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.RIGHT));
    }

    @Override
    public void start(){
        CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.cameraLightSubsystemLeft.setShade(CameraLight.Shades.FULL)));
        CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.cameraLightSubsystemRight.setShade(CameraLight.Shades.FULL)));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        mainController.readButtons();
        robotBase.sorterCameraSubsystem.getAnalysis();
        robotBase.chassisSubsystem.pinpoint.update();
        robotBase.limelightSubsystem.updateLimelight();
        robotBase.chassisSubsystem.drive(mainController.getLeftY(), mainController.getLeftX(), mainController.getRightX(), robotBase.chassisSubsystem.bolSnapToTarget, isFieldCentric, robotBase.limelightSubsystem.getTargetX());
        telemetry.addData("Alliance", DataStorage.alliance);
        telemetry.addData("Heading", robotBase.chassisSubsystem.pinpoint.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Is Field Centric", robotBase.chassisSubsystem.isFieldCentric);
        //telemetry.addData("Intake Power", robotBase.intakeSubsystem.intakeMotor.getPower());
        telemetry.addData("Left Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchLeft());
        telemetry.addData("Middle Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchMiddle());
        telemetry.addData("Right Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchRight());
        telemetry.addData("Distance", robotBase.limelightSubsystem.getHorizontalDistance(-18.5));
        telemetry.addData("Heading Lock", robotBase.chassisSubsystem.bolSnapToTarget);
        telemetry.addData("Is Running", robotBase.limelightSubsystem.limelight.isRunning());
        telemetry.addData("Is connected: ", robotBase.limelightSubsystem.limelight.isConnected());
        telemetry.addData("Hue L", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.LEFT));
        telemetry.addData("Hue M", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.MIDDLE));
        telemetry.addData("Hue R", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.RIGHT));
        telemetry.addData("Sat L", robotBase.sorterCameraSubsystem.getSaturation(SorterCamera.ArtifactSlot.LEFT));
        telemetry.addData("Sat M", robotBase.sorterCameraSubsystem.getSaturation(SorterCamera.ArtifactSlot.MIDDLE));
        telemetry.addData("Sat R", robotBase.sorterCameraSubsystem.getSaturation(SorterCamera.ArtifactSlot.RIGHT));
        telemetry.update();
    }

    @Override
    public void stop(){
        robotBase.limelightSubsystem.limelight.stop();
    }
}
