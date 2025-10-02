package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
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
    RobotBase robotBase;
    @Override
    public void init() {
        robotBase = new RobotBase(hardwareMap);
        robotBase.chassisSubsystem.initLimelight();
        robotBase.chassisSubsystem.changePipline(Chassis.limelightPiplines.REDGOAL);
    }

    @Override
    public void loop() {
        robotBase.chassisSubsystem.updateLimelight();
        if(gamepad1.start){
            robotBase.chassisSubsystem.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, true);
        }
        else{
            robotBase.chassisSubsystem.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, false);
        }
        robotBase.hoodSubsystem.autoSetPosition(chassis.getLaunchAngle());
    }
}
