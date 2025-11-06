package org.firstinspires.ftc.teamcode.opmode;

import android.util.Size;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;
import org.opencv.core.Rect;

@Disabled
@TeleOp(name = "Concept: Vision Color-Sensor", group = "Concept")
public class GoBILDACameraTest extends LinearOpMode
{
    double purpleArtifacts;
    double greenArtifacts;
    PredominantColorProcessor.Result result;
    PredominantColorProcessor.Result resultLeft;
    PredominantColorProcessor.Result resultRight;
    @Override
    public void runOpMode()
    {
        /* Build a "Color Sensor" vision processor based on the PredominantColorProcessor class.
         *
         * - Focus the color sensor by defining a RegionOfInterest (ROI) which you want to inspect.
         *    This can be the entire frame, or a sub-region defined using:
         *    1) standard image coordinates or 2) a normalized +/- 1.0 coordinate system.
         *    Use one form of the ImageRegion class to define the ROI.
         *      ImageRegion.entireFrame()
         *      ImageRegion.asImageCoordinates(50, 50,  150, 150)  100x100 square at the top left corner
         *      ImageRegion.asUnityCenterCoordinates(-0.1, 0.1, 0.1, -0.1)  10% W * H centered square
         *
         * - Set the list of "acceptable" color swatches (matches).
         *     Only colors that you assign here will be returned.
         *     If you know the sensor will be pointing to one of a few specific colors, enter them here.
         *     Or, if the sensor may be pointed randomly, provide some additional colors that may match.
         *     Possible choices are:
         *         RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE, MAGENTA, BLACK, WHITE
         *     Note: For the 2026 season ARTIFACT_PURPLE and ARTIFACT_GREEN have been added.
         *
         *     Note that in the example shown below, only some of the available colors are included.
         *     This will force any other colored region into one of these colors.
         *     eg: Green may be reported as YELLOW, as this may be the "closest" match.
         */
        PredominantColorProcessor colorSensor = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.1, 0.1, 0.1, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .build();

        PredominantColorProcessor colorSensorLeft = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.9, 0.1, -0.7, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .build();

        PredominantColorProcessor colorSensorRight = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(0.7, 0.1, 0.9, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .build();

        result = colorSensor.getAnalysis();
        resultLeft = colorSensorLeft.getAnalysis();
        resultRight = colorSensorRight.getAnalysis();

        /*
         * Build a vision portal to run the Color Sensor process.
         *
         *  - Add the colorSensor process created above.
         *  - Set the desired video resolution.
         *      Since a high resolution will not improve this process, choose a lower resolution
         *      supported by your camera.  This will improve overall performance and reduce latency.
         *  - Choose your video source.  This may be
         *      .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))  .....   for a webcam
         *  or
         *      .setCamera(BuiltinCameraDirection.BACK)    ... for a Phone Camera
         */
        VisionPortal portal = new VisionPortal.Builder()
                .addProcessor(colorSensor)
                .addProcessor(colorSensorLeft)
                .addProcessor(colorSensorRight)
                .setCameraResolution(new Size(640, 480))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();

        telemetry.setMsTransmissionInterval(100);  // Speed up telemetry updates, for debugging.
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);

        // WARNING:  To view the stream preview on the Driver Station, this code runs in INIT mode.
        while (opModeIsActive() || opModeInInit())
        {
            telemetry.addLine("Preview on/off: 3 dots, Camera Stream\n");

            // Request the most recent color analysis.  This will return the closest matching
            // colorSwatch and the predominant color in the RGB, HSV and YCrCb color spaces.
            // The color space values are returned as three-element int[] arrays as follows:
            //  RGB   Red 0-255, Green 0-255, Blue 0-255
            //  HSV   Hue 0-180, Saturation 0-255, Value 0-255
            //  YCrCb Luminance(Y) 0-255, Cr 0-255 (center 128), Cb 0-255 (center 128)
            //
            // Note: to take actions based on the detected color, simply use the colorSwatch or
            // color space value in a comparison or switch.   eg:

            //    if (resultMiddle.closestSwatch == PredominantColorProcessor.Swatch.RED) {.. some code ..}
            //  or:
            //    if (resultMiddle.RGB[0] > 128) {... some code  ...}

            result = colorSensor.getAnalysis();
            resultLeft = colorSensorLeft.getAnalysis();
            resultRight = colorSensorRight.getAnalysis();

            new Trigger(()->result.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                    .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts = purpleArtifacts + 1)))
                    .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts = purpleArtifacts - 1)));

            new Trigger(()->result.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->greenArtifacts++)))
                    .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts--)));

            new Trigger(()->resultLeft.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                    .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts++)))
                    .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts--)));

            new Trigger(()->resultLeft.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->greenArtifacts++)))
                    .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts--)));

            new Trigger(()->resultRight.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                    .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts++)))
                    .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts--)));

            new Trigger(()->resultRight.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->greenArtifacts++)))
                    .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->purpleArtifacts--)));


            // Display the Color Sensor resultMiddle.
            telemetry.addData("Best Match Left", resultLeft.closestSwatch);
            telemetry.addData("Best Match", result.closestSwatch);
            telemetry.addData("Best Match Right", resultRight.closestSwatch);
            telemetry.addData("Purple Artifacts: ", purpleArtifacts);
            telemetry.addData("Green Artifacts: ", greenArtifacts);
            /*telemetry.addLine(String.format("RGB   (%3d, %3d, %3d)",
                    resultMiddle.RGB[0], resultMiddle.RGB[1], resultMiddle.RGB[2]));
            telemetry.addLine(String.format("HSV   (%3d, %3d, %3d)",
                    resultMiddle.HSV[0], resultMiddle.HSV[1], resultMiddle.HSV[2]));
            telemetry.addLine(String.format("YCrCb (%3d, %3d, %3d)",
                    resultMiddle.YCrCb[0], resultMiddle.YCrCb[1], resultMiddle.YCrCb[2]));*/
            telemetry.update();

            sleep(20);
        }
    }
}