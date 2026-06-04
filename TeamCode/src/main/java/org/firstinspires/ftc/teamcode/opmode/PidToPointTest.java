package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.pidtopoint.Chassis;
import org.firstinspires.ftc.teamcode.pidtopoint.Localizer;
import org.firstinspires.ftc.teamcode.pidtopoint.Point;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

@TeleOp(name = "PID To Point Test")
public class PidToPointTest extends OpMode {
    Localizer localizer;
    RobotBase robotBase;
    GamepadEx gamepad;
    Chassis chassis;
    Point testPoint = new Point(2, 0, Math.toRadians(0));
    Point startPoint = new Point(0, 0, 0);
    @Override
    public void init() {
        robotBase = new RobotBase(hardwareMap);
        localizer = new Localizer(robotBase.chassisSubsystem.pinpoint, -7, 3.5);
        gamepad = new GamepadEx(gamepad1);
        chassis = new Chassis(localizer, robotBase.chassisSubsystem.frontLeftMotor, robotBase.chassisSubsystem.frontRightMotor, robotBase.chassisSubsystem.backLeftMotor, robotBase.chassisSubsystem.backRightMotor);
    }

    @Override
    public void start() {
        localizer.pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, startPoint.getX(), startPoint.getY(), AngleUnit.RADIANS, 0));
        chassis.goToPoint(testPoint);
    }

    @Override
    public void loop() {
        gamepad.readButtons();
        chassis.update();
        localizer.pinpoint.update();

        telemetry.addData("X: ", localizer.getX());
        telemetry.addData("Y: ", localizer.getY());
        telemetry.addData("Heading", Math.toDegrees(localizer.getHeadingRad()));
        telemetry.addData("Current Point X", chassis.currentPoint.getX());
        telemetry.addData("Current Point Y", chassis.currentPoint.getY());
        telemetry.addData("Current Point Heading", chassis.currentPoint.getHeadingDeg());
        telemetry.addData("Is At Position", chassis.isAtPosition());
        telemetry.addData("Heading PID Output", chassis.headingPIDOutput);
        telemetry.addData("X Offset < 1", Math.abs(chassis.currentPoint.getX() - localizer.getX()) < 1);
        telemetry.addData("X Offset", Math.abs(chassis.currentPoint.getX() - localizer.getX()));
    }
}
