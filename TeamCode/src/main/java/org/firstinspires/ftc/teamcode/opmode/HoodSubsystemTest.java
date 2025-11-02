package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

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
        robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.BLUEGOAL);
        robotBase.limelightSubsystem.changePipeline(Limelight.limelightPipelines.REDGOAL);
    }

    @Override
    public void loop() {
        robotBase.limelightSubsystem.updateLimelight();
        if(gamepad1.start){
            robotBase.chassisSubsystem.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, true, true, robotBase.limelightSubsystem.getTargetX());
        }
        else{
            robotBase.chassisSubsystem.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, false, true, robotBase.limelightSubsystem.getTargetX());
        }
        double angle = robotBase.limelightSubsystem.getLaunchAngle();
        double verticalComp = robotBase.limelightSubsystem.getVerticalComp();
        double launchSpeed = robotBase.limelightSubsystem.getLaunchSpeed();
        //robotBase.hoodSubsystem.autoSetPosition(angle);
        telemetry.addData("Horizontal Distance", robotBase.limelightSubsystem.getHorizontalDistance(0));
        telemetry.addData("getLaunchAngle", robotBase.limelightSubsystem.getLaunchAngle());
        telemetry.addData("AngleToGoal", robotBase.limelightSubsystem.getAngleToGoal());
        telemetry.addData("Vertical Comp", verticalComp);
        telemetry.addData("Launch Speed", launchSpeed);
        telemetry.addData("Limelight X", robotBase.limelightSubsystem.limelightTX);
        telemetry.addData("Desired Servo Position", robotBase.limelightSubsystem.getLaunchAngle()/300);
    }
}
