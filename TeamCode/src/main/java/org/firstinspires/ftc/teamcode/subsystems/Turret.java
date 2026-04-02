package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;

public class Turret {
    Servo turretServoL;
    Servo turretServoR;
    AnalogInput servoEncoder;
    double turretServoPosition;
    Pose goalPose;
    double xSpeed;
    double ySpeed;
    double timeOfFlight = 0.03;
    double botHeading;
    double turretHeading;
    double turretOffset;
    double rotationLead;
    double maxDegrees;

    public Turret(Servo m_turretServoL, Servo m_turretServoR){
        turretServoL = m_turretServoL;
        turretServoR = m_turretServoR;
    }

    public double convertDegToServoPos(double degree){
        double degreeModulus = degree % 360;
        if(degreeModulus < 0){
            degreeModulus += 360;
        }
        if(degreeModulus > 350){
            degreeModulus = 350;
        }
        return (degreeModulus / 350);
    }

    public double getTurretAngle(GoBildaPinpointDriver pinpoint, Follower follower){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            goalPose = new Pose(144, 138);
        }
        else{
            goalPose = new Pose(144, 138).mirror();
        }
        botHeading = pinpoint.getHeading(AngleUnit.DEGREES);
        xSpeed = pinpoint.getVelX(DistanceUnit.INCH);
        ySpeed = pinpoint.getVelY(DistanceUnit.INCH);
        turretHeading = Math.atan2((goalPose.getY() - follower.getPose().getY() - (ySpeed * timeOfFlight)), (goalPose.getX() - follower.getPose().getX() - (xSpeed * timeOfFlight)));
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            turretOffset = turretHeading - botHeading;
        }
        else{
            turretOffset = botHeading - turretHeading;
        }
        rotationLead = Math.toDegrees(follower.getAngularVelocity()) * timeOfFlight;
        turretOffset += rotationLead;
        turretOffset = ((turretOffset + 180) % 360) -180;
        turretOffset = Math.max(-maxDegrees, Math.min(maxDegrees, turretOffset));
        return turretOffset;
    }

    public void setPosition(double positionDeg){
            turretServoL.setPosition(convertDegToServoPos(positionDeg));
            turretServoR.setPosition(convertDegToServoPos(positionDeg));
            turretServoPosition = convertDegToServoPos(positionDeg);
    }

    public double getPosition(){
        return servoEncoder.getVoltage() / servoEncoder.getMaxVoltage();
    }
}
