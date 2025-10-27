package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;

@TeleOp(name = ("V1 Teleop"))
    public class V1TeleOp extends OpMode {
        public RobotBase robotBase;
        public GamepadEx armController;
        public GamepadEx chassisController;



        @Override
        public void init() {
            CommandScheduler.getInstance().reset();
            robotBase = new RobotBase(hardwareMap);

            robotBase.Intake.intakeMotor.setPower(0);
            robotBase.launcherSubsystem.launcherMotor.setPower(0);
            robotBase.hoodSubsystem.setPosition(0);
            robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.GREEN);
            robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE);

            chassisController = new GamepadEx(gamepad1);
            armController = new GamepadEx(gamepad2);

            chassisController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                    .whileActiveContinuous(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()-> robotBase.Intake.intake(1))
                            .whenFinished(()->CommandScheduler.getInstance().schedule( new InstantCommand(()-> robotBase.Intake.intake(0))))));

            chassisController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                    .whileActiveContinuous(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()-> robotBase.Intake.intake(-1))
                            .whenFinished(()->CommandScheduler.getInstance().schedule( new InstantCommand(()-> robotBase.Intake.intake(0))))));

            chassisController.getGamepadButton(GamepadKeys.Button.B)
                    .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1700))
                            .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))
                            ))));

            chassisController.getGamepadButton(GamepadKeys.Button.X)
                    .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(2000))
                            .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))
                            ))));
            chassisController.getGamepadButton(GamepadKeys.Button.Y)
                    .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(2500))
                            .whenFinished(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))
                            ))));



        }

     @Override
     public void loop() {
        CommandScheduler.getInstance().run();
        chassisController.readButtons();
        telemetry.addData("Motor Velocity", robotBase.launcherSubsystem.getVelocity());
     }
 }