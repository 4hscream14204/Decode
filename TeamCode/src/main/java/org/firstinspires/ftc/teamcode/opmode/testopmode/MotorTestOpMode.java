package org.firstinspires.ftc.teamcode.opmode.testopmode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@TeleOp(name = "Motor Test")
public class MotorTestOpMode extends OpMode {
    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;
    @Override
    public void init() {
        frontLeftMotor = hardwareMap.dcMotor.get("lf");
        frontRightMotor = hardwareMap.dcMotor.get("rf");
        backLeftMotor = hardwareMap.dcMotor.get("lr");
        backRightMotor = hardwareMap.dcMotor.get("rr");
    }

    @Override
    public void loop() {
        if(gamepad1.a){
            frontRightMotor.setPower(1);
        }
        else if(gamepad1.b){
            frontLeftMotor.setPower(1);
        }
        else if(gamepad1.x){
            backRightMotor.setPower(1);
        }
        else if(gamepad1.y){
            backLeftMotor.setPower(1);
        }
        else{
            frontRightMotor.setPower(0);
            frontLeftMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }
        telemetry.addLine("A: Front Right");
        telemetry.addLine("B: Front Left");
        telemetry.addLine("X: Back Right");
        telemetry.addLine("Y: Back Left");
    }
}
