package org.firstinspires.ftc.teamcode.pidtopoint;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.InputMismatchException;

public class Localizer {

    public enum LocalizerChoice{
        PINPOINT,
        OTOS;
    }

    public GoBildaPinpointDriver pinpoint;
    private SparkFunOTOS otos;
    private LocalizerChoice localizerChoice;

    public Localizer(GoBildaPinpointDriver m_pinpoint, double xOffset, double yOffset){
        pinpoint = m_pinpoint;
        pinpoint.setOffsets(xOffset, yOffset, DistanceUnit.INCH);
        localizerChoice = LocalizerChoice.PINPOINT;
    }

    public Localizer(SparkFunOTOS m_otos, double linearScalar, double angularScalar, SparkFunOTOS.Pose2D pose2D){
        otos = m_otos;
        otos.setLinearScalar(linearScalar);
        otos.setAngularScalar(angularScalar);
        otos.setOffset(pose2D);
        localizerChoice = LocalizerChoice.OTOS;
    }

    private LocalizerChoice getLocalizerChoice(){
        return localizerChoice;
    }

    public double getX(){
        if(localizerChoice == LocalizerChoice.PINPOINT){
            return pinpoint.getPosition().getX(DistanceUnit.INCH);
        }
        else if(localizerChoice == LocalizerChoice.OTOS){
            return otos.getPosition().x;
        }
        else{
            throw new InputMismatchException("Must choose Pinpoint or OTOS as a localizer");
        }
    }

    public double getY(){
        if(localizerChoice == LocalizerChoice.PINPOINT){
            return pinpoint.getPosition().getY(DistanceUnit.INCH);
        }
        else if(localizerChoice == LocalizerChoice.OTOS){
            return otos.getPosition().y;
        }
        else{
            throw new InputMismatchException("Must choose Pinpoint or OTOS as a localizer");
        }
    }

    public double getHeadingRad(){
        if(localizerChoice == LocalizerChoice.PINPOINT){
            return pinpoint.getHeading(AngleUnit.RADIANS);
        }
        else if(localizerChoice == LocalizerChoice.OTOS){
            return otos.getPosition().h;
        }
        else{
            throw new InputMismatchException("Must choose Pinpoint or OTOS as a localizer");
        }
    }

}
