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
        double angle = robotBase.chassisSubsystem.getLaunchAngle();
        double verticalComp = robotBase.chassisSubsystem.getVerticalComp();
        double launchSpeed = robotBase.chassisSubsystem.getLaunchSpeed();
        robotBase.hoodSubsystem.autoSetPosition(angle);
        telemetry.addData("Horizontal Distance", robotBase.chassisSubsystem.getHorizontalDistance(0));
        telemetry.addData("getLaunchAngle", robotBase.chassisSubsystem.getLaunchAngle());
        telemetry.addData("AngleToGoal", robotBase.chassisSubsystem.getAngleToGoal());
        telemetry.addData("Vertical Comp", verticalComp);
        telemetry.addData("Launch Speed", launchSpeed);
        telemetry.addData("Limelight X", robotBase.chassisSubsystem.limelightTX);
        telemetry.addData("Desired Servo Position", robotBase.chassisSubsystem.getLaunchAngle()/300);
    }
}
