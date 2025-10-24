package org.firstinspires.ftc.teamcode.robotbase;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.RGBLight;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class RobotBase {

        public RGBLight intakeSubsystem;
        public Launcher launcherSubsystem;
        public Chassis chassisSubsystem;
        public Hood hoodSubsystem;
        public Limelight limelightSubsystem;
        public SorterServo ejectorMiddleSubsystem;
        public SorterServo ejectorLeftSubsystem;
        public SorterServo ejectorRightSubsystem;
        public SorterCamera sorterCameraSubsystem;

        public RobotBase(HardwareMap hwMap) {

            //intakeSubsystem = new Intake(hwMap.dcMotor.get("intake_motor"));
            launcherSubsystem = new Launcher(hwMap.get(DcMotorEx.class, "launcherMotor"));
           //chassisSubsystem = new Chassis(hwMap.dcMotor.get("rf"), hwMap.dcMotor.get("lf"), hwMap.dcMotor.get("rr"), hwMap.dcMotor.get("lr"), hwMap.get(IMU.class, "imu"));
           //hoodSubsystem = new Hood(hwMap.get(Servo.class, "hoodServo"));
           limelightSubsystem = new Limelight(hwMap.get(Limelight3A.class, "limelight"));
           /*ejectorMiddleSubsystem = new SorterServo(hwMap.servo.get("ejectorMiddle"));
           ejectorLeftSubsystem = new SorterServo(hwMap.servo.get("ejectorLeft"));
           ejectorRightSubsystem = new SorterServo(hwMap.servo.get("ejectorRight"));*/

        }
}
