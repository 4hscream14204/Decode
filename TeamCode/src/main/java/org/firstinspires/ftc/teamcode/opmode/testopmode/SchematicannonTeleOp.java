package org.firstinspires.ftc.teamcode.opmode.testopmode;

import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;

@Disabled
@TeleOp(name = "Schematic Cannon")
public class SchematicannonTeleOp extends OpMode {

    RobotBase robotBase;
    GamepadEx chassis;
    IMU imu;
    DcMotorEx launcher1;
    DcMotorEx launcher2;
    DcMotorEx launcher3;
    Intake intake;
    RGBLightSubsystem RGBLightSubsystem;

    public boolean bolTurnToArtifact = false;

    @Override
    public void init() {

        //robotBase = new RobotBase(hardwareMap);
        //robotBase.chassisSubsystem.initLimelight();
        /*launcher1 = hardwareMap.get(DcMotorEx.class, "launcher1");
        launcher2 = hardwareMap.get(DcMotorEx.class,"launcher2");
        launcher3 = hardwareMap.get(DcMotorEx.class, "launcher3");*/
      //  intake = new Intake(hardwareMap.get(DcMotorEx.class, "intake"));
        RGBLightSubsystem = new RGBLightSubsystem(hardwareMap.get(Servo.class,"RGBLightServo"));
        chassis = new GamepadEx(gamepad1);

        new Trigger(()->chassis.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .or(new Trigger(()->chassis.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                        .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                                new InstantCommand(()->intake.intake(gamepad1.left_trigger - gamepad1.right_trigger))))
                                .whenInactive (()->CommandScheduler.getInstance().schedule(
                                                new InstantCommand(()->intake.intake(0))));
chassis.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->RGBLightSubsystem.setColor(org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem.Colors.GREEN))));

        chassis.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->RGBLightSubsystem.setColor(org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem.Colors.PURPLE))));

        chassis.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new InstantCommand(()-> bolTurnToArtifact = !bolTurnToArtifact)
                ));

        chassis.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whileActiveContinuous(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()-> intake.intake(-1))
                        .whenFinished(()->CommandScheduler.getInstance().schedule( new InstantCommand(()-> intake.intake(0))))

                ));

        chassis.getGamepadButton(GamepadKeys.Button.B)
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystemLeft.setPower(-1))
                        .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystemLeft.setPower(0))
                        ))));

        //chassis.getGamepadButton(GamepadKeys.Button.A)
          //      .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(-0.75))
            //            .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setPower(0))
              //          ))));

        chassis.getGamepadButton(GamepadKeys.Button.X)
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystemLeft.setPower(-0.5))
                        .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystemLeft.setPower(0))
                        ))));

        chassis.getGamepadButton(GamepadKeys.Button.Y)
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystemLeft.setPower(-0.2))
                        .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystemLeft.setPower(0))
                        ))));
        /*chassis.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchCommandGroup(launcher1, launcher2,launcher3)));*/


        /*chassis.getGamepadButton(GamepadKeys.Button.BACK)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.chassisSubsystem.cycePiplines())
                )); */
    }

    @Override
    public void loop() {
        chassis.readButtons();
        robotBase.launcherSubsystemLeft.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance());

       // intake.intake(gamepad1.left_trigger / 2 + -1 * gamepad1.right_trigger / 2 );
       // robotBase.launcherSubsystem.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(0));
        //telemetry.addData("id", id);
        //telemetry.addData("tx", robotBase.chassisSubsystem.getTargetX());
        //telemetry.addData("ty", robotBase.chassisSubsystem.getTargetY());
        //telemetry.addData("ta", robotBase.chassisSubsystem.getTargetArea());
        //telemetry.addData("Pipeline type", robotBase.chassisSubsystem.getPiplineType());
        //telemetry.addData("Align With Artifact", bolTurnToArtifact);
        CommandScheduler.getInstance().run();


    }
}
