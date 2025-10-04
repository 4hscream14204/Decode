package org.firstinspires.ftc.teamcode.robotbase;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

public class RobotBase {

        public Intake intakeSubsystem;
        public Launcher launcherSubsystem;
        public Chassis chassisSubsystem;
        public Hood hoodSubsystem;
        public Limelight limelightSubsystem;

        public RobotBase(HardwareMap hwMap) {

            //intakeSubsystem = new Intake(hwMap.dcMotor.get("intake_motor"));
            //launcherSubsystem = new Launcher(hwMap.get(DcMotorEx.class, "launcherMotor"));
           chassisSubsystem = new Chassis(hwMap.dcMotor.get("right_front"), hwMap.dcMotor.get("left_front"), hwMap.dcMotor.get("right_back"), hwMap.dcMotor.get("left_back"));
           hoodSubsystem = new Hood(hwMap.get(Servo.class, "hoodServo"));
           limelightSubsystem = new Limelight(hwMap.get(Limelight3A.class, "limelight"));
        }
}
