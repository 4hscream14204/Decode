package org.firstinspires.ftc.teamcode.robotbase;

import android.graphics.ColorSpace;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.robotbase.prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.subsystems.CameraLight;
import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.IntakeBlocker;
import org.firstinspires.ftc.teamcode.subsystems.ModeLight;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;
import org.firstinspires.ftc.teamcode.subsystems.Tilt;

public class RobotBase {

        public RGBLightSubsystem RGBLightRightSubsystem;
        public RGBLightSubsystem RGBLightMiddleSubsystem;
        public RGBLightSubsystem RGBLightLeftSubsystem;
        public Launcher launcherSubsystemLeft;
        public Launcher launcherSubsystemMiddle;
        public Launcher launcherSubsystemRight;
        public Chassis chassisSubsystem;
        public Limelight limelightSubsystem;
        public SorterServo ejectorMiddleSubsystem;
        public SorterServo ejectorLeftSubsystem;
        public SorterServo ejectorRightSubsystem;
        public SorterCamera sorterCameraSubsystem;
        public Intake intakeSubsystem;
        public CameraLight cameraLightSubsystemLeft;
        public CameraLight cameraLightSubsystemRight;
        public Tilt tiltSubsystem;
        public IntakeBlocker intakeBlockerSubsystem;
        public Hood hoodSubsystem;
        public ModeLight modeLightSubsystem;


        public RobotBase(HardwareMap hwMap) {
            intakeSubsystem = new Intake(hwMap.dcMotor.get("intake_motor"));
            launcherSubsystemLeft = new Launcher(hwMap.get(DcMotorEx.class,
                    "launcherMotorLeft"));
            launcherSubsystemMiddle = new Launcher(hwMap.get(DcMotorEx.class,
                    "launcherMotorMiddle"));
            launcherSubsystemRight = new Launcher(hwMap.get(DcMotorEx.class,
                    "launcherMotorRight"));
           chassisSubsystem = new Chassis(hwMap.dcMotor.get("rf"), hwMap.dcMotor.get("lf"),
                   hwMap.dcMotor.get("rr"), hwMap.dcMotor.get("lr"), hwMap.get(GoBildaPinpointDriver.class, "pinpoint"));
           limelightSubsystem = new Limelight(hwMap.get(Limelight3A.class, "limelight"));
           ejectorMiddleSubsystem = new SorterServo(hwMap.servo.get("transferMiddle"));
           ejectorLeftSubsystem = new SorterServo(hwMap.servo.get("transferLeft"));
           ejectorRightSubsystem = new SorterServo(hwMap.servo.get("transferRight"));
           RGBLightRightSubsystem = new RGBLightSubsystem(hwMap.servo.get("RGBLightRightServo"));
           RGBLightMiddleSubsystem = new RGBLightSubsystem(hwMap.servo.get("RGBLightMiddleServo"));
           RGBLightLeftSubsystem = new RGBLightSubsystem(hwMap.servo.get("RGBLightLeftServo"));
           cameraLightSubsystemLeft = new CameraLight(hwMap.servo.get("cameraLight"));
           sorterCameraSubsystem = new SorterCamera(hwMap.get(WebcamName.class, "SorterCamera"));
            launcherSubsystemLeft.launcherMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            launcherSubsystemMiddle.launcherMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            tiltSubsystem= new Tilt(hwMap.servo.get("TiltServoL"), hwMap.servo.get("tiltServoR"));
            //intakeBlockerSubsystem= new IntakeBlocker(hwMap.servo.get("IntakeBlockerServo"));
            hoodSubsystem = new Hood(hwMap.servo.get("hoodServoL"), hwMap.servo.get("hoodServoR"));
            modeLightSubsystem = new ModeLight(hwMap.get(GoBildaPrismDriver.class, ("modeLight")));



        }
}
