package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;

public class Turret {
    Servo turretServoL;
    Servo turretServoR;
    public AnalogInput servoEncoder;
    double turretServoPosition;
    Pose goalPose;
    double xSpeed;
    double ySpeed;
    double timeOfFlight = 0.03;
    double botHeading;
    double targetHeading;
    double turretOffset;
    double rotationLead;
    double maxDegrees;
    double pidOutputToServoPos;
    PIDController turretHeadingPID = new PIDController(0.01, 0, 0.001);

    public Turret(Servo m_turretServoL, Servo m_turretServoR, AnalogInput m_servoEncoder){
        turretServoL = m_turretServoL;
        turretServoR = m_turretServoR;
        servoEncoder = m_servoEncoder;
    }

    public void setPosition(double position){
        turretServoL.setPosition(position);
        turretServoR.setPosition(position);
    }

    public double convertDegToServoPos(double degree){
        double degreeModulus = degree % 360;
        if(degreeModulus < 0){
            degreeModulus += 360;
        }
        if(degreeModulus < 5){
            degreeModulus = 5;
        }
        if(degreeModulus > 350){
            degreeModulus = 350;
        }
        return ((0.002933 * degreeModulus) - 0.07);
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
        targetHeading = Math.toDegrees(Math.atan2((goalPose.getY() - follower.getPose().getY() - (ySpeed * timeOfFlight)), (goalPose.getX() - follower.getPose().getX() - (xSpeed * timeOfFlight))));
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            turretOffset = targetHeading - botHeading;
        }
        else{
            turretOffset = botHeading - targetHeading;
        }
        //rotationLead = Math.toDegrees(follower.getAngularVelocity()) * timeOfFlight;
        //turretOffset += rotationLead;
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
        double error = (headingDeg - getPositionDegrees());
        double pidOutput = turretHeadingPID.calculate(error);
        pidOutputToServoPos = ((pidOutput + 1) / 2);
        setPosition(pidOutputToServoPos);
    }

    public double getPositionDegrees(){
        return ((-121.454393 * servoEncoder.getVoltage()) + 357.565175);
    }
}
