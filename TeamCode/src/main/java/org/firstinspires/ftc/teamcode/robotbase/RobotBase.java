package org.firstinspires.ftc.teamcode.robotbase;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class RobotBase {

        public Intake intakeSubsystem;
        public Chassis chassisSubsystem;

        public RobotBase(HardwareMap hwMap) {

            intakeSubsystem = new Intake(hwMap.dcMotor.get("intake_motor"));
            chassisSubsystem = new Chassis(hwMap.get(Limelight3A.class, "limelight"), hwMap.dcMotor.get("right_front"), hwMap.dcMotor.get("left_front"), hwMap.dcMotor.get("right_back"), hwMap.dcMotor.get("left_back"));
        }
}
