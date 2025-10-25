package org.firstinspires.ftc.teamcode.robotbase;

import android.graphics.ColorSpace;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class RobotBase {

        public RGBLightSubsystem RGBLightSubsystem;
        public Launcher launcherSubsystem;
        public Chassis chassisSubsystem;
        public Hood hoodSubsystem;
        public Limelight limelightSubsystem;
        public SorterServo ejectorMiddleSubsystem;
        public SorterServo ejectorLeftSubsystem;
        public SorterServo ejectorRightSubsystem;
        public SorterCamera sorterCameraSubsystem;
        public Intake Intake;

        public RobotBase(HardwareMap hwMap) {

            Intake = new Intake(hwMap.dcMotor.get("intake_motor"));
            launcherSubsystem = new Launcher(hwMap.get(DcMotorEx.class, "launcherMotor"));
           chassisSubsystem = new Chassis(hwMap.dcMotor.get("rf"), hwMap.dcMotor.get("lf"), hwMap.dcMotor.get("rr"), hwMap.dcMotor.get("lr"), hwMap.get(IMU.class, "imu"));
           hoodSubsystem = new Hood(hwMap.get(Servo.class, "hoodServo"));
           limelightSubsystem = new Limelight(hwMap.get(Limelight3A.class, "limelight"));
           ejectorMiddleSubsystem = new SorterServo(hwMap.servo.get("ejectorMiddle"));
           ejectorLeftSubsystem = new SorterServo(hwMap.servo.get("ejectorLeft"));
           ejectorRightSubsystem = new SorterServo(hwMap.servo.get("ejectorRight"));
           RGBLightSubsystem = new RGBLightSubsystem(hwMap.servo.get("RGBLightServo"));

        }
}
