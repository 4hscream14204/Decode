package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.List;

public class Chassis extends SubsystemBase {

    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;
    public GoBildaPinpointDriver pinpoint;
    double dblFrontLeftPower;
    double dblFrontRightPower;
    double dblBackLeftPower;
    double dblBackRightPower;

    PIDController headingControl = new PIDController(0.05, 0, 0);

    double dblXOffset;
    public double dblHeadingOutput;

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
    }

    public void drive(double m_gamepadOneLSY, double m_gamepadOneLSX, double m_gamepadOneRSX, boolean m_PIDSteering, boolean isFieldCentric, double m_TX) {
        double dblDenominator;
        double y = (m_gamepadOneLSY * Math.abs(m_gamepadOneLSY) * -1); // Remember, Y stick is reversed!
        double x = m_gamepadOneLSX * Math.abs(m_gamepadOneLSX);
        double rx = m_gamepadOneRSX * Math.abs(m_gamepadOneRSX);
        double botHeading = pinpoint.getHeading(AngleUnit.DEGREES);
        if(isFieldCentric){
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);
            if (m_PIDSteering) {
                dblXOffset = 0 - m_TX;
                dblHeadingOutput = (headingControl.calculate(dblXOffset));
                rx = dblHeadingOutput;
            } else {
                rx = m_gamepadOneRSX * Math.abs(m_gamepadOneRSX);
            }
            dblDenominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            dblFrontLeftPower = (rotY + rotX + rx) / dblDenominator;
            dblBackLeftPower = (rotY - rotX + rx) / dblDenominator;
            dblFrontRightPower = (rotY - rotX - rx) / dblDenominator;
            dblBackRightPower = (rotY + rotX - rx) / dblDenominator;
        }
        else{
            dblDenominator = Math.max(Math.abs(x) + Math.abs(x) + Math.abs(rx), 1);
            dblFrontLeftPower = (y + x + rx) / dblDenominator;
            dblBackLeftPower = (y - x + rx) / dblDenominator;
            dblFrontRightPower = (y - x - rx) / dblDenominator;
            dblBackRightPower = (y + x - rx) / dblDenominator;
        }
        frontLeftMotor.setPower(dblFrontLeftPower);
        frontRightMotor.setPower(dblFrontRightPower);
        backLeftMotor.setPower(dblBackLeftPower);
        backRightMotor.setPower(dblBackRightPower);
    }

    public void resetIMU(){
        pinpoint.recalibrateIMU();
    }
}
