package org.firstinspires.ftc.teamcode.opmode.testopmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferPatternCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferPurpleBallCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferTwoPurpleCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

@Disabled
@TeleOp(name = "SorterSubsystemTest")
public class SorterSubsystemTest extends OpMode {
    SorterServo sorterServoLeft;
    SorterServo sorterServoMiddle;
    SorterServo sorterServoRight;
    SorterCamera camera;
    GamepadEx chassis;
    RobotBase robotBase;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robotBase = new RobotBase(hardwareMap);
        chassis = new GamepadEx(gamepad1);
        camera = new SorterCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        sorterServoLeft = new SorterServo(hardwareMap.servo.get("sorterServoLeft"));
        sorterServoMiddle = new SorterServo(hardwareMap.servo.get("sorterServoMiddle"));
        sorterServoRight = new SorterServo(hardwareMap.servo.get("sorterServoRight"));
        chassis.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new TransferPurpleBallCommandGroup(robotBase)));

        chassis.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferPatternCommandGroup(robotBase)));

        chassis.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferTwoPurpleCommandGroup(robotBase)));


        chassis.getGamepadButton(GamepadKeys.Button.BACK)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()-> DataStorage.pattern = DecodeEnums.Patterns.GPP)));

        chassis.getGamepadButton(GamepadKeys.Button.Y)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->DataStorage.pattern = DecodeEnums.Patterns.PPG)));

        chassis.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->sorterServoLeft.setPosition(SorterServo.ServoPosition.TEST1)), new InstantCommand(()->sorterServoMiddle.setPosition(SorterServo.ServoPosition.TEST1)), new InstantCommand(()->sorterServoRight.setPosition(SorterServo.ServoPosition.TEST1))));

    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        chassis.readButtons();
        camera.getAnalysis();
        telemetry.addData("Pattern: ", DataStorage.pattern);
        //telemetry.addData("Sorter Left", (camera.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE));
        //telemetry.addData("Sorter Middle", (camera.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN));
        //telemetry.addData("Sorter Right", (camera.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE));
    }
}
