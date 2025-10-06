package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import java.util.List;

public class Chassis extends SubsystemBase {

    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;
    IMU imu;

    PIDController headingControl = new PIDController(0.05, 0, 0);

    double dblXOffset;
    public double dblHeadingOutput;

    public Chassis(DcMotor m_frontRightMotor, DcMotor m_frontLeftMotor, DcMotor m_backRightMotor, DcMotor m_backLeftMotor, IMU m_imu) {
        frontLeftMotor = m_frontLeftMotor;
        frontRightMotor = m_frontRightMotor;
        backLeftMotor = m_backLeftMotor;
        backRightMotor = m_backRightMotor;
        imu = m_imu;
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

    }

    public void drive(double m_gamepadOneLSY, double m_gamepadOneLSX, double m_gamepadOneRSX,boolean m_PIDSteering, double m_TX) {
        double y = -m_gamepadOneLSY; // Remember, Y stick is reversed!
        double x = m_gamepadOneLSX * 1.1;
        double rx = 0;
        if (m_PIDSteering) {
            dblXOffset = 0 - m_TX;
            dblHeadingOutput = (headingControl.calculate(dblXOffset));
            rx = dblHeadingOutput;
        } else {
            rx = m_gamepadOneRSX;
        }

        frontLeftMotor.setPower(y + x + rx);
        backLeftMotor.setPower(y - x + rx);
        frontRightMotor.setPower(y - x - rx);
        backRightMotor.setPower(y + x - rx);
    }

    public void resetIMU(){
        imu.resetYaw();
    }
}
