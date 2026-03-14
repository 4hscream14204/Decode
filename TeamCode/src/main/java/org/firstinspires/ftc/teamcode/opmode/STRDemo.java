package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.RobocolConfig;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchPatternCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.ToggleTiltCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferGreenBallCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferPatternCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferPurpleBallCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferTwoPurpleCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

@TeleOp(name = "STR Demo")
public class STRDemo extends OpMode {
    GamepadEx mainController;
    RobotBase robotBase;
    boolean isFieldCentric = true;
    ElapsedTime timer;
    Follower follower;
    Servo prism;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robotBase = new RobotBase(hardwareMap);
        mainController = new GamepadEx(gamepad1);
        robotBase.sorterCameraSubsystem.getAnalysis();
        timer = new ElapsedTime();
        follower = Constants.createFollower(hardwareMap);
        prism = hardwareMap.get(Servo.class, "prism");
        CommandScheduler.getInstance().schedule(new InstantCommand(()->prism.setPosition(0.225)));

        mainController.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .whenPressed(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->isFieldCentric = !isFieldCentric)
                ));

        mainController.getGamepadButton(GamepadKeys.Button.CROSS)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new Transfer3BallsNoCameraCommandGroup(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferPurpleBallCommandGroup(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.SQUARE)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new TransferGreenBallCommandGroup(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferTwoPurpleCommandGroup(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferPatternCommandGroup(robotBase)));

        new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .or(new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)))))
                .whenInactive (()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(0))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.FAR))));

        mainController.getGamepadButton(GamepadKeys.Button.PS)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new ToggleTiltCommandGroup(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystemMiddle.setVelocitySimple(1900))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystemLeft.setVelocitySimple(0)), new InstantCommand(()->robotBase.launcherSubsystemMiddle.setVelocitySimple(0)), new InstantCommand(()->robotBase.launcherSubsystemRight.setVelocitySimple(0))));

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

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorLeftSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH && DataStorage.alliance == DecodeEnums.Alliance.BLUE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorMiddleSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH && DataStorage.alliance == DecodeEnums.Alliance.BLUE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorRightSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH && DataStorage.alliance == DecodeEnums.Alliance.BLUE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorLeftSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH && DataStorage.alliance == DecodeEnums.Alliance.RED)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.RED))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorMiddleSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH && DataStorage.alliance == DecodeEnums.Alliance.RED)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.RED))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorRightSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH && DataStorage.alliance == DecodeEnums.Alliance.RED)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.RED))));
    }

    @Override
    public void start() {
        timer.reset();
        follower.setStartingPose(new Pose(0, 0));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        mainController.readButtons();
        follower.update();
        robotBase.sorterCameraSubsystem.getAnalysis();

        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            robotBase.chassisSubsystem.drive(mainController.getLeftY(), mainController.getLeftX(), mainController.getRightX(), isFieldCentric, timer, robotBase.limelightSubsystem.limelightTX, follower);
        }
        else{
            robotBase.chassisSubsystem.drive(-mainController.getLeftY(), -mainController.getLeftX(), mainController.getRightX(), isFieldCentric, timer, robotBase.limelightSubsystem.getTargetX(), follower);
        }
    }
}
