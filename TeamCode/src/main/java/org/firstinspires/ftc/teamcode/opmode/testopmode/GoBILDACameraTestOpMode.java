package org.firstinspires.ftc.teamcode.opmode.testopmode;

import android.util.Size;

import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

@Disabled
@TeleOp(name = "GoBILDA Camera OpMode")
public class GoBILDACameraTestOpMode extends OpMode {
    double purpleArtifacts;
    double greenArtifacts;
    double emptySpaces;
    PredominantColorProcessor.Result resultMiddle;
    PredominantColorProcessor.Result resultLeft;
    PredominantColorProcessor.Result resultRight;
    PredominantColorProcessor colorSensor;
    PredominantColorProcessor colorSensorLeft;
    PredominantColorProcessor colorSensorRight;
    boolean middleHasBall;
    boolean middlePurple;
    boolean middleGreen;
    boolean leftHasBall;
    boolean leftPurple;
    boolean leftGreen;
    boolean rightHasBall;
    boolean rightPurple;
    boolean rightGreen;
    Servo leftLight;
    Servo rightLight;
    @Override
    public void init() {
        leftLight = hardwareMap.get(Servo.class, "leftSortLight");
        rightLight = hardwareMap.get(Servo.class, "rightSortLight");
        CommandScheduler.getInstance().reset();
        colorSensor = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.1, 0.1, 0.1, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE,
                        PredominantColorProcessor.Swatch.BLACK)
                .build();

        colorSensorLeft = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.9, 0.1, -0.7, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE,
                        PredominantColorProcessor.Swatch.BLACK)
                .build();

        colorSensorRight = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(0.7, 0.1, 0.9, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE,
                        PredominantColorProcessor.Swatch.BLACK)
                .build();

        VisionPortal portal = new VisionPortal.Builder()
                .addProcessor(colorSensor)
                .addProcessor(colorSensorLeft)
                .addProcessor(colorSensorRight)
                .setCameraResolution(new Size(640, 480))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();

        resultMiddle = colorSensor.getAnalysis();
        resultLeft = colorSensorLeft.getAnalysis();
        resultRight = colorSensorRight.getAnalysis();

        new Trigger(()-> resultMiddle.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->middleHasBall = true), new InstantCommand(()->middlePurple = true)))
                .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->middlePurple = false)));

        new Trigger(()->resultMiddle.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->middleHasBall = true), new InstantCommand(()->middleGreen = true)))
                .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->middleGreen = false)));

        new Trigger(()->resultMiddle.closestSwatch != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && resultMiddle.closestSwatch != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->middleHasBall = false)));

        new Trigger(()-> resultLeft.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->leftHasBall = true), new InstantCommand(()->leftPurple = true), new InstantCommand(()->leftLight.setPosition(0.722))))
                .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->leftPurple = false)));

        new Trigger(()->resultLeft.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->leftHasBall = true), new InstantCommand(()->leftGreen = true), new InstantCommand(()->leftLight.setPosition(0.5))))
                .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->leftGreen = false)));

        new Trigger(()->resultLeft.closestSwatch != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && resultLeft.closestSwatch != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->leftHasBall = false), new InstantCommand(()->leftLight.setPosition(0.2783))));

        new Trigger(()-> resultRight.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->rightHasBall = true), new InstantCommand(()->rightPurple = true), new InstantCommand(()->rightLight.setPosition(0.722))))
                .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->rightPurple = false)));

        new Trigger(()->resultRight.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->rightHasBall = true), new InstantCommand(()->rightGreen = true), new InstantCommand(()->rightLight.setPosition(0.5))))
                .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->rightGreen = false)));

        new Trigger(()->resultRight.closestSwatch != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && resultRight.closestSwatch != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->rightHasBall = false), new InstantCommand(()->rightLight.setPosition(0.2783))));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        resultMiddle = colorSensor.getAnalysis();
        resultLeft = colorSensorLeft.getAnalysis();
        resultRight = colorSensorRight.getAnalysis();

        telemetry.addData("Best Match Left", resultLeft.closestSwatch);
        telemetry.addData("Best Match", resultMiddle.closestSwatch);
        telemetry.addData("Best Match Right", resultRight.closestSwatch);
        telemetry.addData("Middle Green?", middleGreen);
        telemetry.addData("Middle Purple?", middlePurple);
        telemetry.addData("Middle Has Ball?", middleHasBall);
        telemetry.addData("Left Green?", leftGreen);
        telemetry.addData("Left Purple?", leftPurple);
        telemetry.addData("Left Has Ball?", leftHasBall);
        telemetry.addData("Right Green?", rightGreen);
        telemetry.addData("Right Purple?", rightPurple);
        telemetry.addData("Right Has Ball?", rightHasBall);
        telemetry.update();
    }
}
