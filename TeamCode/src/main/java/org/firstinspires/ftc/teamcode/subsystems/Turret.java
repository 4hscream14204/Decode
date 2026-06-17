package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.seattlesolvers.solverslib.controller.PIDFController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;

@Configurable
public class Turret {
    Servo turretServoL;
    Servo turretServoR;
    public AnalogInput servoEncoder;
    double turretServoPosition;
    Pose goalPose;
    double xSpeed;
    double ySpeed;
    double timeOfFlight = 0;
    public double botHeading;
    public double targetHeading;
    double turretOffset;
    double rotationLead;
    double maxDegrees;
    public double degreeNormalized;
    public double degreeModulus;
    public double pidOutputToServoPos;
    public double kD = 0.0005;
    public double kP = 0.005;
    public static double kF = 0.1;
    public static double feedForward = 0.1;
    PIDController turretHeadingPID = new PIDController(kP, 0, kD);

    public Turret(Servo m_turretServoL, Servo m_turretServoR, AnalogInput m_servoEncoder){
        turretServoL = m_turretServoL;
        turretServoR = m_turretServoR;
        servoEncoder = m_servoEncoder;
        setPosition(0.5);
    }

    public void setPosition(double position){
        turretServoL.setPosition(position);
        turretServoR.setPosition(position);
    }

    public double convertDegToServoPos(double degree){
        degreeNormalized = AngleUnit.normalizeDegrees(degree);
        degreeModulus = degreeNormalized % 360;
        if(degreeModulus < 0){
            degreeModulus += 360;
        }
        if(degreeModulus < 4){
            degreeModulus = 4;
        }
        if(degreeModulus > 351){
            degreeModulus = 351;
        }
        return ((0.002933 * degreeModulus) - 0.07);
    }

    public double getTurretAngle(GoBildaPinpointDriver pinpoint, Follower follower){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            goalPose = new Pose(144, 140);
        }
        else{
            goalPose = new Pose(-24, 144);
        }
        botHeading = pinpoint.getHeading(AngleUnit.DEGREES);
        xSpeed = pinpoint.getVelX(DistanceUnit.INCH);
        ySpeed = pinpoint.getVelY(DistanceUnit.INCH);
        targetHeading = Math.toDegrees(Math.atan2((goalPose.getY() - follower.getPose().getY() - (ySpeed * timeOfFlight)), (goalPose.getX() - follower.getPose().getX() - (xSpeed * timeOfFlight))));
        turretOffset = targetHeading - botHeading;
        rotationLead = Math.toDegrees(follower.getAngularVelocity()) * timeOfFlight;
        turretOffset += rotationLead;
        //turretOffset = ((turretOffset + 180) % 360) -180;
        //turretOffset = Math.max(-maxDegrees, Math.min(maxDegrees, turretOffset));
        return turretOffset;
    }

    public double getTurretAngle(GoBildaPinpointDriver pinpoint, Follower follower, Pose m_goalPose){
        goalPose = m_goalPose;
        botHeading = pinpoint.getHeading(AngleUnit.DEGREES);
        xSpeed = pinpoint.getVelX(DistanceUnit.INCH);
        ySpeed = pinpoint.getVelY(DistanceUnit.INCH);
        targetHeading = Math.toDegrees(Math.atan2((goalPose.getY() - follower.getPose().getY() - (ySpeed * timeOfFlight)), (goalPose.getX() - follower.getPose().getX() - (xSpeed * timeOfFlight))));
        turretOffset = targetHeading - botHeading;
        rotationLead = Math.toDegrees(follower.getAngularVelocity()) * timeOfFlight;
        turretOffset += rotationLead;
        //turretOffset = ((turretOffset + 180) % 360) -180;
        //turretOffset = Math.max(-maxDegrees, Math.min(maxDegrees, turretOffset));
        return turretOffset;
    }

    public void setPositionDeg(double positionDeg){
            setPosition(convertDegToServoPos(positionDeg));
            //turretServoR.setPosition(convertDegToServoPos(positionDeg));
            turretServoPosition = convertDegToServoPos(positionDeg);
    }

    public void updatePosition(double headingDeg){
        degreeNormalized = (AngleUnit.normalizeDegrees(headingDeg) + 360);
        degreeModulus = degreeNormalized % 360;
        if(degreeModulus < 5){
            degreeModulus = 5;
        }
        if(degreeModulus > 350){
            degreeModulus = 350;
        }
        double error = (degreeModulus - getPositionDegrees());
        double pidOutput = turretHeadingPID.calculate(error);
        pidOutputToServoPos = ((pidOutput + 1) / 2);
        /*if(Math.abs(error) > 2){
            pidOutputToServoPos -= feedForward * (error/Math.abs(error));
        }*/
        setPosition(pidOutputToServoPos);
    }

    public double getPositionDegrees(){
        return ((-121.448569 * servoEncoder.getVoltage()) + 366.747416);
    }

    public boolean isAtPosition(GoBildaPinpointDriver pinpoint, Follower follower){
        if(Math.abs(getTurretAngle(pinpoint, follower) - getPositionDegrees()) < 5){
            return true;
        }
        return false;
    }
}
