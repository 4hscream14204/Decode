package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.seattlesolvers.solverslib.controller.PIDFController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Chassis extends SubsystemBase {

        public DcMotor frontLeftMotor;
        public DcMotor frontRightMotor;
        public DcMotor backLeftMotor;
        public DcMotor backRightMotor;
        public GoBildaPinpointDriver pinpoint;
        double dblFrontLeftPower;
        double dblFrontRightPower;
        double dblBackLeftPower;
        double dblBackRightPower;
        public boolean isFieldCentric;

        PIDFController headingControl = new PIDFController(0.03, 0, 0.001, 0.1);
        PIDFController driveHeadingControl = new PIDFController(2, 0, 0.1, 0.1);
        Pose goalPose = new Pose(127.7, 131.7);
        ElapsedTime timer;

        double dblXOffset;
        public double dblHeadingOutput;
        double headingDeviation;
        public double targetHeading;
        public boolean bolSnapToTarget = false;
        double lastStickTime;
        double currentTime;
        double delayTime = 1000;

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

        public void drive(double m_gamepadOneLSY, double m_gamepadOneLSX, double m_gamepadOneRSX, boolean m_PIDSteering, boolean m_isFieldCentric, double m_TX) {
            double dblDenominator;
            double y = -m_gamepadOneLSY * Math.abs(m_gamepadOneLSY); // Remember, Y stick value is reversed
            double x = m_gamepadOneLSX * Math.abs(m_gamepadOneLSX);
            double rx = m_gamepadOneRSX * Math.abs(m_gamepadOneRSX);
            double botHeading = pinpoint.getHeading(AngleUnit.RADIANS);
            isFieldCentric = m_isFieldCentric;
            if (isFieldCentric) {
                double rotX = m_gamepadOneLSX * Math.cos(-botHeading) - m_gamepadOneLSY * Math.sin(-botHeading);
                double rotY = m_gamepadOneLSX * Math.sin(-botHeading) + m_gamepadOneLSY * Math.cos(-botHeading);
                if (m_PIDSteering) {
                        dblXOffset = 0 - m_TX;
                        dblHeadingOutput = (headingControl.calculate(dblXOffset));
                        rx = dblHeadingOutput;
                    if (m_gamepadOneRSX < -0.5 || m_gamepadOneRSX > 0.5) {
                        bolSnapToTarget = false;
                    }
                } else {
                    rx = m_gamepadOneRSX * Math.abs(m_gamepadOneRSX);
                }
                double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
                dblFrontLeftPower = (rotY + rotX + rx) / denominator;
                dblBackLeftPower = (rotY - rotX + rx) / denominator;
                dblFrontRightPower = (rotY - rotX - rx) / denominator;
                dblBackRightPower = (rotY + rotX - rx) / denominator;
            } else {
                y = -m_gamepadOneLSY;
                dblDenominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
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

        public void drive(double m_gamepadOneLSY, double m_gamepadOneLSX, double m_gamepadOneRSX, boolean m_isFieldCentric, ElapsedTime m_timer) {
            timer = m_timer;
            currentTime = timer.milliseconds();
            double dblDenominator;
            double y = -m_gamepadOneLSY * Math.abs(m_gamepadOneLSY); // Remember, Y stick value is reversed
            double x = m_gamepadOneLSX * Math.abs(m_gamepadOneLSX);
            double rx = m_gamepadOneRSX * Math.abs(m_gamepadOneRSX);
            double botHeading = pinpoint.getHeading(AngleUnit.RADIANS);
            isFieldCentric = m_isFieldCentric;
            if (isFieldCentric) {
                double rotX = m_gamepadOneLSX * Math.cos(-botHeading) - m_gamepadOneLSY * Math.sin(-botHeading);
                double rotY = m_gamepadOneLSX * Math.sin(-botHeading) + m_gamepadOneLSY * Math.cos(-botHeading);
                if (bolSnapToTarget) {

                    if (m_gamepadOneRSX < -0.5 || m_gamepadOneRSX > 0.5) {
                        bolSnapToTarget = false;
                    }
                } else {
                    //targetHeading = botHeading;
                    if (Math.abs(m_gamepadOneRSX) > 0.1) {
                        rx = m_gamepadOneRSX * Math.abs(m_gamepadOneRSX);
                        lastStickTime = currentTime;
                    } else if ((currentTime - lastStickTime) < delayTime) {
                        targetHeading = botHeading;
                    }
                    else if(!bolSnapToTarget){
                        headingDeviation = (botHeading - targetHeading) * -1;
                        headingDeviation = AngleUnit.normalizeRadians(headingDeviation);
                        dblHeadingOutput = driveHeadingControl.calculate(headingDeviation);
                        rx = dblHeadingOutput;
                    }
                }
                double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
                dblFrontLeftPower = (rotY + rotX + rx) / denominator;
                dblBackLeftPower = (rotY - rotX + rx) / denominator;
                dblFrontRightPower = (rotY - rotX - rx) / denominator;
                dblBackRightPower = (rotY + rotX - rx) / denominator;
            } else {
                y = -m_gamepadOneLSY;
                dblDenominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
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

        public void resetIMU() {
            pinpoint.setHeading(0, AngleUnit.DEGREES);
        }
    }
