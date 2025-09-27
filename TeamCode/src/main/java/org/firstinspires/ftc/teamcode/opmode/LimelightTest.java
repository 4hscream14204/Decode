package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Chassis;

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
        robotBase.chassisSubsystem.initLimelight();
        robotBase.chassisSubsystem.changePipline(Chassis.limelightPiplines.REDGOAL);
    }

    public void loop() {
        chassis.readButtons();
        robotBase.chassisSubsystem.updateLimelight();
        robotBase.chassisSubsystem.drive(chassis.getLeftY(), chassis.getLeftX(), chassis.getRightX(), false);

        telemetry.addData("GroundDistance", robotBase.chassisSubsystem.getHorizontalDistance());
        telemetry.addData("AngleToGoal", robotBase.chassisSubsystem.getAngleToGoal());
        telemetry.addData("Pipline Type", robotBase.chassisSubsystem.getPiplineType());

        CommandScheduler.getInstance().run();
    }
}
