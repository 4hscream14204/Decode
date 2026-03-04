package org.firstinspires.ftc.teamcode.subsystems;

import static android.os.SystemClock.sleep;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import android.util.Size;

import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.robotbase.PrismAnimations;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

import java.util.concurrent.TimeUnit;

public class SorterCamera extends SubsystemBase {
    WebcamName webcam;
    PredominantColorProcessor colorSensor;
    PredominantColorProcessor colorSensorLeft;
    PredominantColorProcessor colorSensorRight;
    public PredominantColorProcessor.Result resultMiddle;
    public PredominantColorProcessor.Result resultLeft;
    public PredominantColorProcessor.Result resultRight;
    public boolean isMiddleAndLeftPurple = false;
    public boolean isRightAndLeftPurple = false;
    public boolean isMiddleAndRightPurple = false;
    public int intMinSaturation = 15;
    public PredominantColorProcessor.Swatch leftColor = PredominantColorProcessor.Swatch.BLACK;
    public PredominantColorProcessor.Swatch middleColor = PredominantColorProcessor.Swatch.BLACK;
    public PredominantColorProcessor.Swatch rightColor = PredominantColorProcessor.Swatch.BLACK;

    int blackCountLeft = 0;
    int colorCountLeft = 0;
    int blackCountMiddle = 0;
    int colorCountMiddle = 0;
    int blackCountRight = 0;
    int colorCountRight = 0;

    int blackCountThreshold = 7;
    int colorCountThreshold = 3;

    long exposure = 3;

    GainControl gainControl;
    ExposureControl exposureControl;

    public enum Colors{
        GREENHIGH (109),
        GREENLOW (45),
        PURPLEHIGH (165),
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
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.1, -0.05, 0.3, -0.35))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE,
                        PredominantColorProcessor.Swatch.BLACK)
                .build();

        colorSensorLeft = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(-.65, -0.05, -0.63, -0.25))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.WHITE,
                        PredominantColorProcessor.Swatch.BLACK)
                .build();

        colorSensorRight = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asUnityCenterCoordinates(0.85, -0.05, 0.9, -0.25))
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

        CommandScheduler.getInstance().schedule(new SequentialCommandGroup(new WaitUntilCommand(()->portal.getCameraState() == VisionPortal.CameraState.STREAMING),
                new InstantCommand(()->gainControl = portal.getCameraControl(GainControl.class)),
                new InstantCommand(()->exposureControl = portal.getCameraControl(ExposureControl.class)),
                new InstantCommand(()->exposureControl.setMode(ExposureControl.Mode.Manual)),
                new InstantCommand(()->exposureControl.setExposure(exposure, TimeUnit.MILLISECONDS)),
                new InstantCommand(()->gainControl.setGain(0))));

    }
    public void getAnalysis(){
        resultMiddle = colorSensor.getAnalysis();
        resultLeft = colorSensorLeft.getAnalysis();
        resultRight = colorSensorRight.getAnalysis();

        if (getUnfilteredColor(ArtifactSlot.LEFT) == PredominantColorProcessor.Swatch.BLACK) {
            blackCountLeft ++;
            colorCountLeft = 0;
            if((leftColor != PredominantColorProcessor.Swatch.BLACK) && blackCountLeft > blackCountThreshold) {
                leftColor = PredominantColorProcessor.Swatch.BLACK;
            }
        } else {
            colorCountLeft ++;
            blackCountLeft = 0;
            if((leftColor == PredominantColorProcessor.Swatch.BLACK) && colorCountLeft > colorCountThreshold) {
                leftColor = getUnfilteredColor(ArtifactSlot.LEFT);
            } else if (leftColor != PredominantColorProcessor.Swatch.BLACK){
                leftColor = getUnfilteredColor(ArtifactSlot.LEFT);
            }
        }

        if(getUnfilteredColor(ArtifactSlot.MIDDLE) == PredominantColorProcessor.Swatch.BLACK) {
            blackCountMiddle ++;
            colorCountMiddle = 0;
            if((middleColor != PredominantColorProcessor.Swatch.BLACK) && blackCountMiddle > blackCountThreshold) {
                middleColor = PredominantColorProcessor.Swatch.BLACK;
            }
        } else {
            colorCountMiddle ++;
            blackCountMiddle = 0;
            if((middleColor == PredominantColorProcessor.Swatch.BLACK) && colorCountMiddle > colorCountThreshold) {
                middleColor = getUnfilteredColor(ArtifactSlot.MIDDLE);
            } else if (middleColor != PredominantColorProcessor.Swatch.BLACK){
                middleColor = getUnfilteredColor(ArtifactSlot.MIDDLE);
            }
        }

        if(getUnfilteredColor(ArtifactSlot.RIGHT) == PredominantColorProcessor.Swatch.BLACK) {
            blackCountRight ++;
            colorCountRight = 0;
            if((rightColor != PredominantColorProcessor.Swatch.BLACK) && blackCountRight > blackCountThreshold) {
                rightColor = getUnfilteredColor(ArtifactSlot.RIGHT);
            }
        } else {
            colorCountRight ++;
            blackCountRight = 0;
            if ((rightColor == PredominantColorProcessor.Swatch.BLACK) && colorCountRight > colorCountThreshold) {
                rightColor = getUnfilteredColor(ArtifactSlot.RIGHT);
            } else if (rightColor != PredominantColorProcessor.Swatch.BLACK) {
                rightColor = getUnfilteredColor(ArtifactSlot.RIGHT);
            }
        }
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

    public int getSaturation(ArtifactSlot m_slot){
        if(m_slot == ArtifactSlot.LEFT){
            return resultLeft.HSV[1];
        }

        if(m_slot == ArtifactSlot.MIDDLE){
            return resultMiddle.HSV[1];
        }

        return resultRight.HSV[1];
    }

    public PredominantColorProcessor.Swatch getUnfilteredColor(ArtifactSlot m_slot){

        if(getSaturation(m_slot) > intMinSaturation) {
            if ((getHue(m_slot) > Colors.GREENLOW.value) && (getHue(m_slot) < Colors.GREENHIGH.value)) {
                return PredominantColorProcessor.Swatch.ARTIFACT_GREEN;
            } else if ((getHue(m_slot) > Colors.PURPLELOW.value) && (getHue(m_slot) < Colors.PURPLEHIGH.value)) {
                return PredominantColorProcessor.Swatch.ARTIFACT_PURPLE;
            }
        }

        return PredominantColorProcessor.Swatch.BLACK;
    }

    public PredominantColorProcessor.Swatch getColor(ArtifactSlot m_slot) {
        if (m_slot == ArtifactSlot.LEFT) {
            return leftColor;
        } else if (m_slot == ArtifactSlot.MIDDLE) {
            return middleColor;
        } else {
            return rightColor;
        }
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

    public long getExposure(){
        if(exposureControl != null) {
            return exposureControl.getExposure(TimeUnit.MILLISECONDS);
        } else {
            return -43572987;
        }
    }
}
