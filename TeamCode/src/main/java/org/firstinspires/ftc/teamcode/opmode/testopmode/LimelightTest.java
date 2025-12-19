package org.firstinspires.ftc.teamcode.opmode.testopmode;

import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

@Disabled
@TeleOp(name = "LimelightTest")
public class LimelightTest extends OpMode {

    RobotBase robotBase;
    GamepadEx chassis;

    @Override
    public void init() {

        robotBase = new RobotBase(hardwareMap);

        chassis = new GamepadEx(gamepad1);

        robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.BLUEGOAL);
        robotBase.limelightSubsystem.changePipeline(Limelight.limelightPipelines.REDGOAL);
        robotBase.chassisSubsystem.resetIMU();

    }

    
    public void loop() {
        chassis.readButtons();
        robotBase.limelightSubsystem.updateLimelight();
        //robotBase.limelightSubsystem.limelight.updateRobotOrientation(robotBase.chassisSubsystem.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        //robotBase.chassisSubsystem.drive(chassis.getLeftY(), chassis.getLeftX(), chassis.getRightX(), false, true, robotBase.limelightSubsystem.getTargetX());

        telemetry.addData("GroundDistance", robotBase.limelightSubsystem.getHorizontalDistance(0));
        telemetry.addData("Vertical Comp", robotBase.limelightSubsystem.getVerticalComp());
        telemetry.addData("Vertical Distance", robotBase.limelightSubsystem.getVerticalDistance(17));
        telemetry.addData("Arc Time", robotBase.limelightSubsystem.getArcTime());
        telemetry.addData("AngleToGoalRad", Math.toRadians(robotBase.limelightSubsystem.getAngleToGoal()));
        telemetry.addData("AngleToGoal", robotBase.limelightSubsystem.getAngleToGoal());
        telemetry.addData("Pipline Type", robotBase.limelightSubsystem.getPipline());
        telemetry.addData("Target Y", robotBase.limelightSubsystem.getTargetY());
        //telemetry.addData("Heading", robotBase.chassisSubsystem.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        //telemetry.addData("Robot Orientation", robotBase.limelightSubsystem.limelight.updateRobotOrientation(robotBase.chassisSubsystem.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)));
        telemetry.addData("Velocity", robotBase.limelightSubsystem.getLaunchSpeed());
        telemetry.addData("Angle", robotBase.limelightSubsystem.getLaunchAngle());

        CommandScheduler.getInstance().run();
    }
}
