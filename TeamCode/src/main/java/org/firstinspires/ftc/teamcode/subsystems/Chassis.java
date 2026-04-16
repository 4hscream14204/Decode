package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.skeletonarmy.marrow.zones.Point;
import com.skeletonarmy.marrow.zones.PolygonZone;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;

public class Chassis{

    public DcMotor frontLeftMotor;
    public DcMotor frontRightMotor;
    public DcMotor backLeftMotor;
    public DcMotor backRightMotor;
    public GoBildaPinpointDriver pinpoint;
    double dblFrontLeftPower;
    double dblFrontRightPower;
    double dblBackLeftPower;
    double dblBackRightPower;
    public boolean isFieldCentric = true;
    public boolean isInCloseZone;
    public boolean isInFarZone;
    PolygonZone closeLaunchZone;
    PolygonZone farLaunchZone;
    PolygonZone robotZone;

    PIDFController headingControl = new PIDFController(0.02, 0, 0.002, 0.05);
    PIDFController driveHeadingControl = new PIDFController(2, 0, 0.1, 0.1);
    Pose goalPose = new Pose(127.7, 131.7);
    ElapsedTime timer;
    Follower follower;
    double distance;

    double dblXOffset;
    public double dblHeadingOutput;
    double headingDeviation;
    public double targetHeading;
    public boolean bolSnapToTarget = false;
    double lastStickTime;
    double currentTime;
    double delayTime = 1000;
    double xSpeed;
    double ySpeed;
    double timeOfFlightMultiplier = 0.003/*0.003*/;
    double timeOfFlight;
    double changeThreshold = 0.01;

    public Chassis(DcMotor m_frontRightMotor, DcMotor m_frontLeftMotor, DcMotor m_backRightMotor, DcMotor m_backLeftMotor, GoBildaPinpointDriver m_pinpoint) {
        frontLeftMotor = m_frontLeftMotor;
        frontRightMotor = m_frontRightMotor;
        backLeftMotor = m_backLeftMotor;
        backRightMotor = m_backRightMotor;
        pinpoint = m_pinpoint;
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        closeLaunchZone = new PolygonZone(new Point(144, 130), new Point(72, 65), new Point(0, 130));
        farLaunchZone = new PolygonZone(new Point(45, 0), new Point(72, 36), new Point(100, 0));
        robotZone = new PolygonZone(13, 10);
    }

    public void drive(double m_gamepadOneLSY, double m_gamepadOneLSX, double m_gamepadOneRSX, boolean m_isFieldCentric, Follower m_follower) {
        follower = m_follower;
        double dblDenominator;
        double y = -m_gamepadOneLSY * Math.abs(m_gamepadOneLSY); // Remember, Y stick value is reversed
        double x = m_gamepadOneLSX * Math.abs(m_gamepadOneLSX);
        double rx = m_gamepadOneRSX * Math.abs(m_gamepadOneRSX);
        double botHeading = pinpoint.getHeading(AngleUnit.RADIANS);
        updateRobotZone();
        isFieldCentric = m_isFieldCentric;
        if (isFieldCentric) {
            if(m_gamepadOneRSX < -0.1){
                rx = Math.max(m_gamepadOneRSX * Math.abs(m_gamepadOneRSX), -0.75);
            }
            else if(m_gamepadOneRSX > 0.1){
                rx = Math.min(m_gamepadOneRSX * Math.abs(m_gamepadOneRSX), 0.75);
            }
            //y = -m_gamepadOneLSY * Math.abs(m_gamepadOneLSY);
            double rotX = m_gamepadOneLSX * Math.cos(-botHeading) - m_gamepadOneLSY * Math.sin(-botHeading);
            double rotY = m_gamepadOneLSX * Math.sin(-botHeading) + m_gamepadOneLSY * Math.cos(-botHeading);
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            dblFrontLeftPower = (rotY + rotX + rx) / denominator;
            dblBackLeftPower = (rotY - rotX + rx) / denominator;
            dblFrontRightPower = (rotY - rotX - rx) / denominator;
            dblBackRightPower = (rotY + rotX - rx) / denominator;
        } else {
            //y = -m_gamepadOneLSY;
            dblDenominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            dblFrontLeftPower = (y + x + rx) / dblDenominator;
            dblBackLeftPower = (y - x + rx) / dblDenominator;
            dblFrontRightPower = (y - x - rx) / dblDenominator;
            dblBackRightPower = (y + x - rx) / dblDenominator;
        }

        if ((Math.abs(dblFrontLeftPower - frontLeftMotor.getPower()) > changeThreshold) ||
                (Math.abs(dblFrontRightPower - frontRightMotor.getPower()) > changeThreshold) ||
                (Math.abs(dblBackLeftPower - backLeftMotor.getPower()) > changeThreshold) ||
                (Math.abs(dblBackRightPower - backRightMotor.getPower()) > changeThreshold)) {
            frontLeftMotor.setPower(dblFrontLeftPower);
            frontRightMotor.setPower(dblFrontRightPower);
            backLeftMotor.setPower(dblBackLeftPower);
            backRightMotor.setPower(dblBackRightPower);
        }
    }

    public void setTargetHeading(double degrees){
        targetHeading = Math.toRadians(degrees);
    }

    public void resetIMU() {
        pinpoint.setHeading(0, AngleUnit.DEGREES);
    }

    public void updateRobotZone(){
        robotZone.setRotation(follower.getHeading());
        robotZone.setPosition(follower.getPose().getX(), follower.getPose().getY());
    }

    public boolean isInCloseZone(){
        if(robotZone.isInside(closeLaunchZone)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isInFarZone(){
        if(robotZone.isInside(farLaunchZone)){
            return true;
        }
        else{
            return false;
        }
    }
}