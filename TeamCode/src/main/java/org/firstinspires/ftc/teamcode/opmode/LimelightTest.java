package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
    }

    public void start() {
        robotBase.limelightSubsystem.initLimelight();
        robotBase.limelightSubsystem.changePipline(Limelight.limelightPiplines.REDGOAL);
    }

    public void loop() {
        chassis.readButtons();
        robotBase.limelightSubsystem.updateLimelight();
        robotBase.chassisSubsystem.drive(chassis.getLeftY(), chassis.getLeftX(), chassis.getRightX(), false, robotBase.limelightSubsystem.getTargetX());

        telemetry.addData("GroundDistance", robotBase.limelightSubsystem.getHorizontalDistance(0));
        telemetry.addData("AngleToGoal", robotBase.limelightSubsystem.getAngleToGoal());
        telemetry.addData("Pipline Type", robotBase.limelightSubsystem.getPiplineType());
        telemetry.addData("Target Y", robotBase.limelightSubsystem.getTargetY());

        CommandScheduler.getInstance().run();
    }
}
