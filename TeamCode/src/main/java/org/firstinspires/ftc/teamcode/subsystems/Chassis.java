package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import java.util.List;

public class Chassis extends SubsystemBase {

    public enum limelightPiplines {
        APRILTAG(2),
        ARTIFACT(0);
        public final int value;
        limelightPiplines(int m_val){
            this.value = m_val;
        }
    }

    public Limelight3A limelight;
    DcMotor frontLeftMotor;
    DcMotor frontRightmotor;
    DcMotor backLeftmotor;
    DcMotor backRightmotor;
    public double limelightTX;
    public double limelightTY;
    public String limelightPiplineType;
    public double limelightTa;

    public limelightPiplines enmLimelightPiplines;

    PIDController headingControl = new PIDController(0.05, 0, 0);

    double dblXOffset;
    public double dblHeadingOutput;

    public Chassis(Limelight3A m_limelight, DcMotor m_frontRightMotor, DcMotor m_frontLeftMotor, DcMotor m_backRightMotor, DcMotor m_backLeftMotor) {
        limelight = m_limelight;
        frontLeftMotor = m_frontLeftMotor;
        frontRightmotor = m_frontRightMotor;
        backLeftmotor = m_backLeftMotor;
        backRightmotor = m_backRightMotor;
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftmotor.setDirection(DcMotor.Direction.REVERSE);
        /*
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)); */

    }

    public void initLimelight() {

        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.pipelineSwitch(0);
        limelight.start();



        LLResult result = limelight.getLatestResult();
        List<LLResultTypes.ColorResult> fiducials = result.getColorResults();
        int id = 0;
        for(LLResultTypes.ColorResult fiducial :fiducials) {
            double limelightx = fiducial.getTargetXDegrees(); // Where it is (left-right)
            double limelighty = fiducial.getTargetYDegrees(); // Where it is (up-down)
            double StrafeDistance_3D = fiducial.getRobotPoseTargetSpace().getPosition().y;

            //telemetry.addData("Fiducial " + id, "is " + StrafeDistance_3D + " meters away");
        }
    }

    public void updateLimelight(){
        LLResult result = limelight.getLatestResult();
        limelightTX = result.getTx();
        limelightTY = result.getTy();
        limelightPiplineType = result.getPipelineType();
        limelightTa = result.getTa();
    }

    public void changePipline(limelightPiplines m_pipline){
        enmLimelightPiplines = m_pipline;
        limelight.pipelineSwitch(m_pipline.value);
    }

    public void cycePiplines(){
        if(enmLimelightPiplines == limelightPiplines.ARTIFACT) {
            changePipline(limelightPiplines.APRILTAG);
        } else {
            changePipline(limelightPiplines.ARTIFACT);
        }
    }

    public double getTargetX(){
        return limelightTX;
    }

    public double getTargetY() {
        return limelightTY;
    }

    public String getPiplineType() {
        return limelightPiplineType;
    }

    public double getTargetArea() {
        return limelightTa;
    }

    public void drive(double m_gamepadOneLSY, double m_gamepadOneLSX, double m_gamepadOneRSX,boolean m_PIDSteering) {
        double y = -m_gamepadOneLSY; // Remember, Y stick is reversed!
        double x = m_gamepadOneLSX * 1.1;
        double rx = 0;
        if (m_PIDSteering) {
            dblXOffset = 0 - limelightTX;
            dblHeadingOutput = (headingControl.calculate(dblXOffset));
            rx = dblHeadingOutput;
        } else {
            rx = m_gamepadOneRSX;
        }

        frontLeftMotor.setPower(y + x + rx);
        backLeftmotor.setPower(y - x + rx);
        frontRightmotor.setPower(y - x - rx);
        backRightmotor.setPower(y + x - rx);
    }
}
