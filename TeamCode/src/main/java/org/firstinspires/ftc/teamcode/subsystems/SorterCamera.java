package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import android.util.Size;

import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class SorterCamera extends SubsystemBase {
    WebcamName webcam;
    PredominantColorProcessor colorSensor;
    PredominantColorProcessor colorSensorLeft;
    PredominantColorProcessor colorSensorRight;
    PredominantColorProcessor.Result resultMiddle;
    PredominantColorProcessor.Result resultLeft;
    PredominantColorProcessor.Result resultRight;
    public int intPurple = 0;
    public boolean isMiddleAndLeftPurple = false;
    public boolean isRightAndLeftPurple = false;
    public boolean isMiddleAndRightPurple = false;
    public SorterCamera(WebcamName m_webcam){
        webcam = m_webcam;
        colorSensor = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.1, 0.1, 0.1, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE,
                        PredominantColorProcessor.Swatch.BLACK)
                .build();

        colorSensorLeft = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.7, 0.1, -0.5, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE,
                        PredominantColorProcessor.Swatch.BLACK)
                .build();

        colorSensorRight = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(0.6, 0.1, 0.8, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE,
                        PredominantColorProcessor.Swatch.BLACK)
                .build();

        VisionPortal portal = new VisionPortal.Builder()
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(colorSensor)
                .addProcessor(colorSensorLeft)
                .addProcessor(colorSensorRight)
                .setCameraResolution(new Size(640, 480))
                .setCamera(m_webcam)
                .build();
    }
    public void getAnalysis(){
        resultMiddle = colorSensor.getAnalysis();
        resultLeft = colorSensorLeft.getAnalysis();
        resultRight = colorSensorRight.getAnalysis();
    }

    public PredominantColorProcessor.Swatch getClosestSwatchMiddle(){
        return resultMiddle.closestSwatch;
    }

    public PredominantColorProcessor.Swatch getClosestSwatchLeft(){
        return resultLeft.closestSwatch;
    }

    public PredominantColorProcessor.Swatch getClosestSwatchRight(){
        return resultRight.closestSwatch;
    }

    public boolean hasTwoPurple(){
        if(resultMiddle.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && resultLeft.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE){
           isMiddleAndLeftPurple = true;
           isMiddleAndRightPurple = false;
           isRightAndLeftPurple = false;
           return true;
        }
        else if(resultMiddle.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && resultRight.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE){
            isMiddleAndRightPurple = true;
            isMiddleAndLeftPurple = false;
            isRightAndLeftPurple = false;
            return true;
        }
        else if(resultLeft.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && resultRight.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE){
            isRightAndLeftPurple = true;
            isMiddleAndLeftPurple = false;
            isMiddleAndRightPurple = false;
            return true;
        }
        else{
            isMiddleAndRightPurple = false;
            isMiddleAndLeftPurple = false;
            isRightAndLeftPurple = false;
            return false;
        }
    }

    public boolean hasThreeArtifacts(){
        if((resultMiddle.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN ||
                resultMiddle.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                && (resultLeft.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN
                || resultLeft.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                && (resultRight.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN
                || resultRight.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)){
            return true;
        }
        else{
            return false;
        }
    }

}
