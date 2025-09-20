package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import java.util.List;

@TeleOp(name = "AlignToBall")
public class AlignToBallTest extends OpMode {

    PIDController headingControl = new PIDController(0.05, 0, 0);

    private Limelight3A limelight;
    GamepadEx chassis;
    IMU imu;
    DcMotor frontLeftMotor;
    DcMotor frontRightmotor;
    DcMotor backLeftmotor;
    DcMotor backRightmotor;

    public boolean bolTurnToArtifact = false;
    public double dblXOffset;
    public double botHeading;
    public double dblHeadingOutput;

    public void toggleTurnToArtifact(){
        bolTurnToArtifact = !bolTurnToArtifact;
    }

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
        limelight.pipelineSwitch(0);
        limelight.start(); // This tells Limelight to start looking!

        chassis.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new InstantCommand(()-> toggleTurnToArtifact())
                ));
    }

    @Override
    public void loop() {
        chassis.readButtons();

        LLResult result = limelight.getLatestResult();
        List<LLResultTypes.ColorResult> fiducials = result.getColorResults();
        int id = 0;
        for (LLResultTypes.ColorResult fiducial : fiducials) {
            double limelightx = fiducial.getTargetXDegrees(); // Where it is (left-right)
            double limelighty = fiducial.getTargetYDegrees(); // Where it is (up-down)
            double StrafeDistance_3D = fiducial.getRobotPoseTargetSpace().getPosition().y;

            //telemetry.addData("Fiducial " + id, "is " + StrafeDistance_3D + " meters away");
        }

        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
        double x = gamepad1.left_stick_x * 1.1;
        double rx = 0;
        if (bolTurnToArtifact) {
            dblXOffset = 0 - result.getTx();
            dblHeadingOutput = (headingControl.calculate(dblXOffset));
            rx = dblHeadingOutput;
        } else {
            rx = gamepad1.right_stick_x;
        }

        frontLeftMotor.setPower(y + x + rx);
        backLeftmotor.setPower(y - x + rx);
        frontRightmotor.setPower(y - x - rx);
        backRightmotor.setPower(y + x - rx);

       // telemetry.addData("id", id);
        telemetry.addData("tx", result.getTx());
        telemetry.addData("ty", result.getTy());
        telemetry.addData("ta", result.getTa());
        telemetry.addData("Pipeline type", result.getPipelineType());
        telemetry.addData("Align With Artifact", bolTurnToArtifact);
        CommandScheduler.getInstance().run();
    }
}
