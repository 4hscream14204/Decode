package org.firstinspires.ftc.teamcode.base;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class RobotBase {
    Intake intakeSubsystem;
    Chassis chassisSubsystem;
    public RobotBase(HardwareMap hwMap){
        intakeSubsystem = new Intake(hwMap.dcMotor.get("intakeMotor"), hwMap.dcMotor.get("intakeMotor2"));
        chassisSubsystem = new Chassis(hwMap.dcMotor.get("frontRightMotor"), hwMap.dcMotor.get("frontLeftMotor"), hwMap.dcMotor.get("backRightMotor"), hwMap.dcMotor.get("backLeftMotor"), hwMap.get(GoBildaPinpointDriver.class, "pinpoint"));
    }
}
