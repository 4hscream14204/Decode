package org.firstinspires.ftc.teamcode.base;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.IntakeDistanceSensor;
import org.firstinspires.ftc.teamcode.subsystems.IntakeTransfer;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.LiftDistanceSensor;
import org.firstinspires.ftc.teamcode.subsystems.Prism;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

import java.util.List;

public class RobotBase {
    public IntakeTransfer intakeTransferSubsystem;
    public Chassis chassisSubsystem;
    public IntakePivot intakePivotSubsystem;
    public Launcher launcherSubsystem;
    public Turret turretSubsystem;
    public LiftDistanceSensor liftDistanceSensorSubsystem;
    public IntakeDistanceSensor intakeLIntakeDistanceSensorSubsystem;
    public IntakeDistanceSensor intakeRIntakeDistanceSensorSubsystem;
    public Lift liftSubsystem;
    public TransferBlocker transferBlockerSubsystem;
    public Hood hoodSubsystem;
    public Prism prismSubsystem;
    public List<VoltageSensor> voltageSensor;
    VoltageSensor controlHubVoltageSensor;

    public RobotBase(HardwareMap hwMap){
        voltageSensor = hwMap.getAll(VoltageSensor.class);
        controlHubVoltageSensor = voltageSensor.get(0);
        intakeTransferSubsystem = new IntakeTransfer(hwMap.dcMotor.get("intakeMotor"), hwMap.dcMotor.get("transferMotor"));
        chassisSubsystem = new Chassis(hwMap.dcMotor.get("frontRightMotor"), hwMap.dcMotor.get("frontLeftMotor"), hwMap.dcMotor.get("backRightMotor"), hwMap.dcMotor.get("backLeftMotor"), hwMap.get(GoBildaPinpointDriver.class, "pinpoint"));
        intakePivotSubsystem = new IntakePivot(hwMap.servo.get("intakePivotServo"));
        launcherSubsystem = new Launcher(hwMap.get(DcMotorEx.class, "launcherMotor"), hwMap.get(DcMotorEx.class, "launcherMotor2"), controlHubVoltageSensor);
        turretSubsystem = new Turret(hwMap.servo.get("turretServoL"), hwMap.servo.get("turretServoR"), hwMap.analogInput.get("turretEncoder"));
        liftDistanceSensorSubsystem = new LiftDistanceSensor(hwMap.analogInput.get("liftDistanceSensor"));
        intakeLIntakeDistanceSensorSubsystem = new IntakeDistanceSensor(hwMap.analogInput.get("intakeLSensor"));
        intakeRIntakeDistanceSensorSubsystem = new IntakeDistanceSensor(hwMap.analogInput.get("intakeRSensor"));
        transferBlockerSubsystem = new TransferBlocker(hwMap.servo.get("transferBlocker"));
        hoodSubsystem = new Hood(hwMap.servo.get("hoodServo"));
        prismSubsystem = new Prism(hwMap.servo.get("prism"));
    }
}
