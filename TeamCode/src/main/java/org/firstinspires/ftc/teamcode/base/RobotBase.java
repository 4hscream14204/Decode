package org.firstinspires.ftc.teamcode.base;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.IntakeDistanceSensor;
import org.firstinspires.ftc.teamcode.subsystems.IntakeTransfer;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.LiftDistanceSensor;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

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

    public RobotBase(HardwareMap hwMap){
        intakeTransferSubsystem = new IntakeTransfer(hwMap.dcMotor.get("intakeMotor"), hwMap.dcMotor.get("transferMotor"));
        chassisSubsystem = new Chassis(hwMap.dcMotor.get("frontRightMotor"), hwMap.dcMotor.get("frontLeftMotor"), hwMap.dcMotor.get("backRightMotor"), hwMap.dcMotor.get("backLeftMotor"), hwMap.get(GoBildaPinpointDriver.class, "pinpoint"));
        intakePivotSubsystem = new IntakePivot(hwMap.servo.get("intakePivotServo"));
        launcherSubsystem = new Launcher(hwMap.get(DcMotorEx.class, "launcherMotor"));
        turretSubsystem = new Turret(hwMap.servo.get("turretServoL"), hwMap.servo.get("turretServoR"));
        liftDistanceSensorSubsystem = new LiftDistanceSensor(hwMap.analogInput.get("liftDistanceSensor"));
        intakeLIntakeDistanceSensorSubsystem = new IntakeDistanceSensor(hwMap.digitalChannel.get("intakeLDistanceSensor"));
        intakeRIntakeDistanceSensorSubsystem = new IntakeDistanceSensor(hwMap.digitalChannel.get("intakeRDistanceSensor"));
        liftSubsystem = new Lift(hwMap.servo.get("liftServoL"));
    }
}
