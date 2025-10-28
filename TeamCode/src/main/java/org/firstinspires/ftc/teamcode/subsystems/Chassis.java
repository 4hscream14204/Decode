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

    }

    public void drive(double m_gamepadOneLSY, double m_gamepadOneLSX, double m_gamepadOneRSX, boolean m_PIDSteering, boolean isFieldCentric, double m_TX) {
        double dblDenominator;
        double y = -m_gamepadOneLSY; // Remember, Y stick is reversed!
        double x = (m_gamepadOneLSX * Math.abs(m_gamepadOneLSX) * -1);
        double rx;
        double botHeading = pinpoint.getHeading(AngleUnit.DEGREES);
        if (m_PIDSteering) {
            dblXOffset = 0 - m_TX;
            dblHeadingOutput = (headingControl.calculate(dblXOffset));
            rx = dblHeadingOutput;
        } else {
            rx = m_gamepadOneRSX * Math.abs(m_gamepadOneRSX);
        }
        if(isFieldCentric){
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);
            dblDenominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            frontLeftMotor.setPower((rotY + rotX + rx) / dblDenominator);
            backLeftMotor.setPower((rotY - rotX + rx) / dblDenominator);
            frontRightMotor.setPower((rotY - rotX - rx) / dblDenominator);
            backRightMotor.setPower((rotY + rotX - rx) / dblDenominator);
        }
        else{
            dblDenominator = Math.max(Math.abs(m_gamepadOneLSX) + Math.abs(m_gamepadOneLSX) + Math.abs(rx), 1);
            double dblFrontLeftPower = (m_gamepadOneLSY + m_gamepadOneLSX + rx) / dblDenominator;
            double dblBackLeftPower = (m_gamepadOneLSY - m_gamepadOneLSX + rx) / dblDenominator;
            double dblFrontRightPower = (m_gamepadOneLSY - m_gamepadOneLSX - rx) / dblDenominator;
            double dblBackRightPower = (m_gamepadOneLSY + m_gamepadOneLSX - rx) / dblDenominator;
            frontLeftMotor.setPower(dblFrontLeftPower);
            backLeftMotor.setPower(dblBackLeftPower);
            frontRightMotor.setPower(dblFrontRightPower);
            backRightMotor.setPower(dblBackRightPower);
        }
    }

    public void resetIMU(){
        pinpoint.recalibrateIMU();
    }
}
