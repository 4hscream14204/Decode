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
    public PredominantColorProcessor.Result resultMiddle;
    public PredominantColorProcessor.Result resultLeft;
    public PredominantColorProcessor.Result resultRight;
    public int intPurple = 0;
    public boolean isMiddleAndLeftPurple = false;
    public boolean isRightAndLeftPurple = false;
    public boolean isMiddleAndRightPurple = false;

    public enum Colors{
        GREENHIGH (109),
        GREENLOW (80),
        PURPLEHIGH (138),
        PURPLELOW (109);
        public final double value;
        Colors(double m_colorAmounts){this.value = m_colorAmounts;}
    }

    public enum ArtifactSlot{
        LEFT,
        RIGHT,
        MIDDLE;
    }

    public SorterCamera(WebcamName m_webcam){
        webcam = m_webcam;
        colorSensor = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.15, 0.1, 0.3, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE,
                        PredominantColorProcessor.Swatch.BLACK)
                .build();

        colorSensorLeft = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.7, 0.1, -0.35, -0.1))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE,
                        PredominantColorProcessor.Swatch.BLACK)
                .build();

        colorSensorRight = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(0.4, 0.1, 0.8, -0.1))
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

    public int getHue(ArtifactSlot m_slot){
        if(m_slot == ArtifactSlot.LEFT){
            return resultLeft.HSV[0];
        }

        if(m_slot == ArtifactSlot.MIDDLE){
            return resultMiddle.HSV[0];
        }

        return resultRight.HSV[0];
    }

    public PredominantColorProcessor.Swatch getColor(ArtifactSlot m_slot){
        if((getHue(m_slot) > Colors.GREENLOW.value) && (getHue(m_slot) < Colors.GREENHIGH.value)){
            return PredominantColorProcessor.Swatch.ARTIFACT_GREEN;
        } else if((getHue(m_slot) > Colors.PURPLELOW.value) && (getHue(m_slot) < Colors.PURPLEHIGH.value)) {
            return PredominantColorProcessor.Swatch.ARTIFACT_PURPLE;
        }

        return PredominantColorProcessor.Swatch.BLACK;
    }

    public PredominantColorProcessor.Swatch getClosestSwatchMiddle(){
        return getColor(ArtifactSlot.MIDDLE);
    }

    public PredominantColorProcessor.Swatch getClosestSwatchLeft(){
        return getColor(ArtifactSlot.LEFT);
    }

    public PredominantColorProcessor.Swatch getClosestSwatchRight(){
        return getColor(ArtifactSlot.RIGHT);
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
