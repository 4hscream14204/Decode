package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.base.CustomGamepad;
import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.commands.DynamicVelocityCommand;
import org.firstinspires.ftc.teamcode.commands.TurretHeadingControlCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.Prism;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;

import java.util.List;

@TeleOp(name = "Thwimp TeleOp")
public class ThwimpTeleOp extends OpMode {
    RobotBase robotBase;
    Follower follower;
    GamepadEx mainController;
    GamepadEx launcherController;
    CustomGamepad mainControllerCG;
    CustomGamepad launcherControllerCG;
    TelemetryManager telemetryM;
    List<LynxModule> allHubs;
    Pose goalPose = new Pose(144, 138);
    ElapsedTime timer;
    boolean readyToLaunch;
    int artifactsInBotCount;
    @Override
    public void init() {
        timer = new ElapsedTime();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        allHubs = hardwareMap.getAll(LynxModule.class);
        for(LynxModule hub : allHubs){
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
        CommandScheduler.getInstance().reset();
        robotBase = new RobotBase(hardwareMap);
        mainController = new GamepadEx(gamepad1);
        launcherController = new GamepadEx(gamepad2);
        follower = Constants.createFollower(hardwareMap);

        mainControllerCG = new CustomGamepad(mainController, robotBase, follower);
        mainControllerCG.player1();

        launcherControllerCG = new CustomGamepad(launcherController, robotBase, follower);
        launcherControllerCG.player2();

        robotBase.chassisSubsystem.frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .or(new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER))), new InstantCommand(()->robotBase.intakeTransferSubsystem.transfer((mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER))/2))))
                .whenInactive (()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(0)), new InstantCommand(()->robotBase.intakeTransferSubsystem.transfer(0))));

        new Trigger(()->timer.seconds() > 110)
                .whileActiveOnce(new InstantCommand(()->robotBase.prismSubsystem.setPosition(Prism.PrismModes.PARK)));

        new Trigger(()->!robotBase.turretSubsystem.isAtPosition(robotBase.chassisSubsystem.pinpoint, follower))
                .whenActive(new InstantCommand(()->robotBase.prismSubsystem.setAllianceColor()))
                .whenInactive(new InstantCommand(()->robotBase.prismSubsystem.rainbow()));


        /*new Trigger(()->robotBase.chassisSubsystem.isInCloseZone())
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.close())))
                .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.far())));*/

        /*new Trigger(()->readyToLaunch)
                .whenActive(()->CommandScheduler.getInstance().schedule(new TransferCommand(robotBase, follower)));*/
        new Trigger(()->robotBase.intakeLIntakeDistanceSensorSubsystem.getDistance() <= 6 && robotBase.intakeRIntakeDistanceSensorSubsystem.getDistance() <= 7)
                .whenActive(()->artifactsInBotCount++);

        new Trigger(()->artifactsInBotCount == 3)
                .whenActive(()->mainController.gamepad.rumble(1, 1, 500));

        new Trigger(()->artifactsInBotCount == 3)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()-> robotBase.chassisSubsystem.setTargetHeading(0)), new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK))));
    }

    @Override
    public void start(){
        robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK);
        robotBase.prismSubsystem.setPosition(Prism.PrismModes.RAINBOW);
        follower.setStartingPose(new Pose(92, 10, Math.toRadians(0)));
        robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE);
        robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE);
        CommandScheduler.getInstance().schedule(new TurretHeadingControlCommandGroup(robotBase, follower));
        //robotBase.turretSubsystem.updatePosition(90);
        CommandScheduler.getInstance().schedule(new DynamicVelocityCommand(robotBase, follower));
        timer.reset();
    }

    @Override
    public void loop() {

        for(LynxModule hub : allHubs){
            hub.clearBulkCache();
        }

        if(robotBase.turretSubsystem.isAtPosition(robotBase.chassisSubsystem.pinpoint, follower) && robotBase.chassisSubsystem.isInCloseZone()){
            readyToLaunch = true;
        }
        else{
            readyToLaunch = false;
        }

        follower.update();
        mainController.readButtons();
        launcherController.readButtons();
        //robotBase.chassisSubsystem.pinpoint.update();
        robotBase.chassisSubsystem.drive(mainController.getLeftY(), mainController.getLeftX(), mainController.getRightX(), true, follower, timer);
        robotBase.chassisSubsystem.updateRobotZone();
        robotBase.hoodSubsystem.setDynamicPosition(follower.getPose().distanceFrom(goalPose));
        //CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.turretSubsystem.setPositionDeg(90)));

        telemetry.addData("Pinpoint heading", robotBase.chassisSubsystem.pinpoint.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Intake Left: ", robotBase.intakeLIntakeDistanceSensorSubsystem.getDistance());
        telemetry.addData("Intake Right: ", robotBase.intakeRIntakeDistanceSensorSubsystem.getDistance());
        telemetry.addData("Calc Turret Angle", robotBase.turretSubsystem.getTurretAngle(robotBase.chassisSubsystem.pinpoint, follower));
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Distance: ", follower.getPose().distanceFrom(goalPose));
        telemetry.addData("Launch Velocity Calc", robotBase.launcherSubsystem.getLaunchVelocity(follower.getPose().distanceFrom(goalPose)));
        telemetry.addData("Ready to Launch", readyToLaunch);
        telemetry.addData("Artifacts in Bot: ", artifactsInBotCount);
        CommandScheduler.getInstance().run();
    }
}
