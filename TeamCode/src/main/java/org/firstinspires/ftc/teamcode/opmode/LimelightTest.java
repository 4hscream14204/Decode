package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

@TeleOp(name = "LimelightTest")
public class LimelightTest extends OpMode {

    RobotBase robotBase;
    GamepadEx chassis;

    @Override
    public void init() {

        robotBase = new RobotBase(hardwareMap);

        chassis = new GamepadEx(gamepad1);

        robotBase.limelightSubsystem.initLimelight();
        robotBase.limelightSubsystem.changePipline(Limelight.limelightPiplines.REDGOAL);
        robotBase.chassisSubsystem.resetIMU();
    }

    
    public void loop() {
        chassis.readButtons();
        robotBase.limelightSubsystem.updateLimelight();
        robotBase.limelightSubsystem.limelight.updateRobotOrientation(robotBase.chassisSubsystem.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        robotBase.chassisSubsystem.drive(chassis.getLeftY(), chassis.getLeftX(), chassis.getRightX(), false, robotBase.limelightSubsystem.getTargetX());

        telemetry.addData("GroundDistance", robotBase.limelightSubsystem.getHorizontalDistance(0));
        telemetry.addData("AngleToGoalRad", Math.toRadians(robotBase.limelightSubsystem.getAngleToGoal()));
        telemetry.addData("AngleToGoal", robotBase.limelightSubsystem.getAngleToGoal());
        telemetry.addData("Pipline Type", robotBase.limelightSubsystem.getPipline());
        telemetry.addData("Target Y", robotBase.limelightSubsystem.getTargetY());
        telemetry.addData("Heading", robotBase.chassisSubsystem.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        telemetry.addData("Robot Orientation", robotBase.limelightSubsystem.limelight.updateRobotOrientation(robotBase.chassisSubsystem.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)));

        CommandScheduler.getInstance().run();
    }
}
