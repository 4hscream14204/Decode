package org.firstinspires.ftc.teamcode.opmode.testopmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
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
        robotBase.chassisSubsystem.drive(chassis.getLeftY(), chassis.getLeftX(), chassis.getRightX(), false, true, robotBase.limelightSubsystem.getTargetX());


        CommandScheduler.getInstance().run();
    }
}
