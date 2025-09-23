package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

import java.util.List;

@TeleOp(name = "Schematic Cannon")
public class SchematicannonTeleOp extends OpMode {

    RobotBase robotBase;
    GamepadEx chassis;
    IMU imu;

    public boolean bolTurnToArtifact = false;
    //public double dblXOffset;
    public double botHeading;
    //public double dblHeadingOutput;

    @Override
    public void init() {

        robotBase = new RobotBase(hardwareMap);
        //robotBase.chassisSubsystem.initLimelight();
        /*
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)); */

        chassis = new GamepadEx(gamepad1);

        chassis.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new InstantCommand(()-> bolTurnToArtifact = !bolTurnToArtifact)
                ));

        chassis.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whileActiveContinuous(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()-> robotBase.intakeSubsystem.intake(-1))
                        .whenFinished(()->CommandScheduler.getInstance().schedule( new InstantCommand(()-> robotBase.intakeSubsystem.intake(0))))

                ));

        chassis.getGamepadButton(GamepadKeys.Button.B)
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(-1))
                        .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(0))
                        ))));

        chassis.getGamepadButton(GamepadKeys.Button.A)
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(-0.75))
                        .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(0))
                        ))));

        chassis.getGamepadButton(GamepadKeys.Button.X)
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(-0.5))
                        .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(0))
                        ))));

        chassis.getGamepadButton(GamepadKeys.Button.Y)
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(-0.2))
                        .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(0))
                        ))));
    }

    @Override
    public void loop() {
        chassis.readButtons();

        // telemetry.addData("id", id);
       // telemetry.addData("tx", robotBase.chassisSubsystem.getTargetX());
        //telemetry.addData("ty", robotBase.chassisSubsystem.getTargetY());
        //telemetry.addData("ta", robotBase.chassisSubsystem.getTargetArea());
        //telemetry.addData("Pipeline type", robotBase.chassisSubsystem.getPiplineType());
        //telemetry.addData("Align With Artifact", bolTurnToArtifact);
        CommandScheduler.getInstance().run();
    }
}
