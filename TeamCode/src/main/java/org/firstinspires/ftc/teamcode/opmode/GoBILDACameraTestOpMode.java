package org.firstinspires.ftc.teamcode.opmode;

import static com.arcrobotics.ftclib.kotlin.extensions.gamepad.GamepadExExtKt.whenActive;

import android.util.Size;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

@TeleOp(name = "GoBILDA Camera OpMode")
public class GoBILDACameraTestOpMode extends OpMode {
    double purpleArtifacts;
    double greenArtifacts;
    double emptySpaces;
    PredominantColorProcessor.Result result;
    PredominantColorProcessor.Result resultLeft;
    PredominantColorProcessor.Result resultRight;
    PredominantColorProcessor colorSensor;
    PredominantColorProcessor colorSensorLeft;
    PredominantColorProcessor colorSensorRight;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        colorSensor = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.1, 0.1, 0.1, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE)
                .build();

        colorSensorLeft = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.9, 0.1, -0.7, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE)
                .build();

        colorSensorRight = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(0.7, 0.1, 0.9, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE)
                .build();

        VisionPortal portal = new VisionPortal.Builder()
                .addProcessor(colorSensor)
                .addProcessor(colorSensorLeft)
                .addProcessor(colorSensorRight)
                .setCameraResolution(new Size(640, 480))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();

        result = colorSensor.getAnalysis();
        resultLeft = colorSensorLeft.getAnalysis();
        resultRight = colorSensorRight.getAnalysis();

        new Trigger(()->result.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts++)));

        new Trigger(()->result.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->greenArtifacts++)));

        new Trigger(()->resultLeft.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts++)));

        new Trigger(()->resultLeft.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->greenArtifacts++)));

        new Trigger(()->resultRight.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts++)));

        new Trigger(()->resultRight.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->greenArtifacts++)));

        new Trigger(()->result.closestSwatch == PredominantColorProcessor.Swatch.WHITE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->emptySpaces++), new InstantCommand(()->purpleArtifacts--), new InstantCommand(()->greenArtifacts--)));

        new Trigger(()->resultLeft.closestSwatch == PredominantColorProcessor.Swatch.WHITE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->emptySpaces++), new InstantCommand(()->purpleArtifacts--), new InstantCommand(()->greenArtifacts--)));

        new Trigger(()->resultRight.closestSwatch == PredominantColorProcessor.Swatch.WHITE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->emptySpaces++), new InstantCommand(()->purpleArtifacts--), new InstantCommand(()->greenArtifacts--)));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        result = colorSensor.getAnalysis();
        resultLeft = colorSensorLeft.getAnalysis();
        resultRight = colorSensorRight.getAnalysis();

        telemetry.addData("Best Match Left", resultLeft.closestSwatch);
        telemetry.addData("Best Match", result.closestSwatch);
        telemetry.addData("Best Match Right", resultRight.closestSwatch);
        telemetry.addData("Purple Artifacts: ", purpleArtifacts);
        telemetry.addData("Green Artifacts: ", greenArtifacts);
        telemetry.update();
    }
}
