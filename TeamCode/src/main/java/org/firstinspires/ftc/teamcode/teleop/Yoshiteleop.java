package org.firstinspires.ftc.teamcode.teleop;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.subsystems.Claw;

@TeleOp(name = "Yoshi")
public class Yoshiteleop extends OpMode {
    DcMotor frontLeftMotor;
    DcMotor frontRightmotor;
    DcMotor backLeftmotor;
    DcMotor backRightmotor;
    GamepadEx chassis;
    IMU imu;
    @Override
    public void init(){
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
        Claw clawSubsystem = new Claw(hardwareMap.servo.get("clawServo"))

        chassis.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()-> clawSubsystem.toggleClaw())));
    }

    @Override
    public void loop(){
        chassis.readButtons();
        double y = -chassis.getLeftY();
        double x = chassis.getLeftX()*1.1;
        double rx = chassis.getRightX();
        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotx = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double roty = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotx = rotx * 1.1;

        double denominator = Math.max(Math.abs(roty) + Math.abs(rx), 1);
        double frontLeftPower = (roty + rotx + rx)/ denominator;
        double backLeftPower = (roty - rotx + rx)/ denominator;
        double frontRightPower = (roty - rotx - rx)/ denominator;
        double backRightPower = (roty + rotx - rx)/ denominator;

        frontLeftMotor.setPower(frontLeftPower);
        backLeftmotor.setPower(backLeftPower);
        frontRightmotor.setPower(frontRightPower);
        backRightmotor.setPower(backRightPower);
    }
}
