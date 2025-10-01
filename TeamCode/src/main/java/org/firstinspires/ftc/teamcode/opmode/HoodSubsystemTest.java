package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Hood;

@TeleOp(name = "Hood Subsystem Test")
public class HoodSubsystemTest extends OpMode {
    Limelight3A limelight3A;
    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;
    Servo hoodServo;
    Chassis chassis;
    Hood hood;
    @Override
    public void init() {
        chassis = new Chassis(limelight3A, frontRightMotor, frontLeftMotor, backRightMotor, backLeftMotor);
        hood = new Hood(hoodServo);
    }

    @Override
    public void loop() {
        hood.autoSetPosition(chassis.getLaunchAngle());
    }
}
