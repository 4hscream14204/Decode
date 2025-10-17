package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.commandgroups.LaunchCommandGroup;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

@TeleOp(name = "Schematic Cannon")
public class SchematicannonTeleOp extends OpMode {

    RobotBase robotBase;
    GamepadEx chassis;
    IMU imu;
    DcMotorEx launcher1;
    DcMotorEx launcher2;
    DcMotorEx launcher3;
    DcMotorEx intake;

    public boolean bolTurnToArtifact = false;

    @Override
    public void init() {

        robotBase = new RobotBase(hardwareMap);
        //robotBase.chassisSubsystem.initLimelight();
        launcher1 = hardwareMap.get(DcMotorEx.class, "launcher1");
        launcher2 = hardwareMap.get(DcMotorEx.class,"launcher2");
        launcher3 = hardwareMap.get(DcMotorEx.class, "launcher3");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

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
        chassis.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchCommandGroup(launcher1, launcher2,launcher3)));


        /*chassis.getGamepadButton(GamepadKeys.Button.BACK)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.chassisSubsystem.cycePiplines())
                )); */
    }

    @Override
    public void loop() {
        chassis.readButtons();

        //telemetry.addData("id", id);
        //telemetry.addData("tx", robotBase.chassisSubsystem.getTargetX());
        //telemetry.addData("ty", robotBase.chassisSubsystem.getTargetY());
        //telemetry.addData("ta", robotBase.chassisSubsystem.getTargetArea());
        //telemetry.addData("Pipeline type", robotBase.chassisSubsystem.getPiplineType());
        //telemetry.addData("Align With Artifact", bolTurnToArtifact);
        CommandScheduler.getInstance().run();
    }
}
