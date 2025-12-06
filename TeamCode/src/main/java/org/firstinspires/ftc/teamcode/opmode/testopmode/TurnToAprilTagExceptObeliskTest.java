package org.firstinspires.ftc.teamcode.opmode.testopmode;


import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.subsystems.Limelight;

import java.util.List;

@Disabled
@TeleOp(name = "Heading Lock No Obelisk")
public class TurnToAprilTagExceptObeliskTest extends OpMode {

    PIDController headingControl = new PIDController(0.05, 0, 0);

    private Limelight3A limelight;
    GamepadEx chassis;
    IMU imu;
    DcMotor frontLeftMotor;
    DcMotor frontRightmotor;
    DcMotor backLeftmotor;
    DcMotor backRightmotor;

    boolean isOnBlueAlliance = true;

    public boolean bolTurnToGoal = false;
    public double dblXOffset;
    public double botHeading;
    public double dblHeadingOutput;

    /*public double angleToGoal(double targetY){
        return mountingAngle + targetY;
    }

    public double getHorizontalDistance(double ty){
        return ((goalHeight - limelightHeight) / Math.tan(Math.toRadians(angleToGoal(ty))));
    }

    public void getGroundAngle(double tx){

    }*/

    public void turnToGoal(){

    }

    public void toggleTurnToGoal(){
        bolTurnToGoal = !bolTurnToGoal;
    }

    @Override
    public void init() {
        frontLeftMotor = hardwareMap.dcMotor.get("leftFront");
        frontRightmotor = hardwareMap.dcMotor.get("rightFront");
        backLeftmotor = hardwareMap.dcMotor.get("leftRear");
        backRightmotor = hardwareMap.dcMotor.get("rightRear");
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        chassis = new GamepadEx(gamepad1);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.pipelineSwitch(Limelight.limelightPipelines.REDGOAL.value);
        limelight.start(); // This tells Limelight to start looking!

        chassis.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()-> toggleTurnToGoal())
                ));

        chassis.getGamepadButton(GamepadKeys.Button.BACK)
                .whenPressed(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->isOnBlueAlliance = !isOnBlueAlliance)
                ));
    }

    public void loop() {
        chassis.readButtons();

        LLResult result = limelight.getLatestResult();
        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        int id = 0;
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            id = fiducial.getFiducialId(); // The ID number of the fiducial
            double limelightx = fiducial.getTargetXDegrees(); // Where it is (left-right)
            double limelighty = fiducial.getTargetYDegrees(); // Where it is (up-down)
            double StrafeDistance_3D = fiducial.getRobotPoseTargetSpace().getPosition().y;

            //Matelemetry.addData("Fiducial " + id, "is " + StrafeDistance_3D + " meters away");
        }

        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
        double x = gamepad1.left_stick_x * 1.1;
        double rx = 0;
        if (bolTurnToGoal && (isOnBlueAlliance && id == 20 || !isOnBlueAlliance && id == 24)) {
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

        telemetry.addData("id", id);
        telemetry.addData("tx", result.getTx());
        telemetry.addData("ty", result.getTy());
        telemetry.addData("ta", result.getTa());
        telemetry.addData("Pipeline type", result.getPipelineType());
        telemetry.addData("Align With April Tag", bolTurnToGoal);
        CommandScheduler.getInstance().run();
    }
}
