package org.firstinspires.ftc.teamcode.opmode;


import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

@TeleOp(name = "Color Sensor TeleOP")
public class ColorSensorTest extends OpMode {

    RobotBase robotBase;

    /*PredominantColorProcessor colorSensor = new PredominantColorProcessor.Builder()
            .setRoi(ImageRegion.asUnityCenterCoordinates(-0.1, 0.1, 0.1, -0.1))
            .setSwatches(
                    PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                    PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                    PredominantColorProcessor.Swatch.RED,
                    PredominantColorProcessor.Swatch.BLUE,
                    PredominantColorProcessor.Swatch.YELLOW,
                    PredominantColorProcessor.Swatch.BLACK,
                    PredominantColorProcessor.Swatch.WHITE)
            .build();*/

    @Override
    public void init () {
        robotBase = new RobotBase(hardwareMap);

        /*VisionPortal portal = new VisionPortal.Builder()
                .addProcessor(colorSensor)
                .setCameraResolution(new Size(640, 480))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();

        telemetry.setMsTransmissionInterval(100);
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);*/

    }

    public void loop() {

        //PredominantColorProcessor.Result result = colorSensor.getAnalysis();

        telemetry.addData("ColorHue", robotBase.colorSensorSubsystem.getHueValues());
        //telemetry.addData("Distance", robotBase.lidarSubsystem.getDistance());
        //telemetry.addData("Color", result.closestSwatch);
    }
}