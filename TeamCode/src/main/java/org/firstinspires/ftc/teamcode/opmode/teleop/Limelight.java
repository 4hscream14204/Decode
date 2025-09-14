package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Network;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.List;

@TeleOp(name = "Limelight")
public class Limelight extends OpMode {

    private Limelight3A limelight;
    GamepadEx chassis;
    IMU imu;
    DcMotor frontLeftMotor;
    DcMotor frontRightmotor;
    DcMotor backLeftmotor;
    DcMotor backRightmotor;

    @Override
    public void init() {

        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        frontRightmotor = hardwareMap.dcMotor.get("frontRightMotor");
        backLeftmotor = hardwareMap.dcMotor.get("backLeftMotor");
        backRightmotor = hardwareMap.dcMotor.get("backRightMotor");
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        chassis = new GamepadEx(gamepad1);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.pipelineSwitch(2);
        limelight.start(); // This tells Limelight to start looking!
    }

    @Override
    public void loop(){

        chassis.readButtons();
        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        frontLeftMotor.setPower(y + x + rx);
        backLeftmotor.setPower(y - x + rx);
        frontRightmotor.setPower(y - x - rx);
        backRightmotor.setPower(y + x - rx);

        LLResult result = limelight.getLatestResult();
        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        int id = 0;
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            id = fiducial.getFiducialId(); // The ID number of the fiducial
            double limelightx = fiducial.getTargetXDegrees(); // Where it is (left-right)
            double limelighty = fiducial.getTargetYDegrees(); // Where it is (up-down)
            double StrafeDistance_3D = fiducial.getRobotPoseTargetSpace().getPosition().y;

            telemetry.addData("Fiducial " + id, "is " + StrafeDistance_3D + " meters away");
        }
        telemetry.addData("id", id);
        telemetry.addData("tx", result.getTx());
        telemetry.addData("ty", result.getTy());
        telemetry.addData("ta", result.getTa());
        telemetry.addData("Pipeline type", result.getPipelineType());
    }
}