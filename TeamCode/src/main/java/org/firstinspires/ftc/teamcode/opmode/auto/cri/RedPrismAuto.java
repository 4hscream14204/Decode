package org.firstinspires.ftc.teamcode.opmode.auto.cri;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.commands.AutoTurretHeadingCommand;
import org.firstinspires.ftc.teamcode.commands.DynamicVelocityAutoCommand;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;


@Autonomous(name = "Red Prism Auto")
public class RedPrismAuto extends OpMode {

    Follower follower;
    RobotBase robotBase;
    SequentialCommandGroup path;
    int artifactsInBotCount;
    GamepadEx gamepad;
    ElapsedTime timer;

    int waitTime= 0;

    Pose startPose = new Pose(62, 185, Math.toRadians(-90)).mirror();
   Pose goalPose = new Pose(144, 138);
//144 138
    BezierLine startToLaunch = new BezierLine(startPose, new Pose(70, 150, Math.toRadians(-90)).mirror());




    PathChain startLaunch;



    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);
        robotBase = new RobotBase(hardwareMap);
        gamepad = new GamepadEx(gamepad1);
        timer = new ElapsedTime();

        startLaunch = follower.pathBuilder()
                .addPath(startToLaunch)
                .setLinearHeadingInterpolation(startPose.getHeading(), Math.toRadians(-90))
                //.addParametricCallback(0.97, ()->CommandScheduler.getInstance().schedule(new WaitCommand(1000)))
                .addParametricCallback(0.25, ()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.2))))
                .addParametricCallback(0.75, ()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer())))
                .build();


        path = new SequentialCommandGroup(
                new WaitUntilCommand(()->waitTime <= timer.milliseconds()),
                new FollowPathCommand(follower, startLaunch, true,1),
                new WaitCommand(800),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(800),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK))
        );

        new Trigger(()->robotBase.intakeLIntakeDistanceSensorSubsystem.getDistance() <= 6 &&
                robotBase.intakeRIntakeDistanceSensorSubsystem.getDistance() <= 7)
                .whenActive(()->artifactsInBotCount++);

        new Trigger(()->artifactsInBotCount > 2)
                .whenActive(new SequentialCommandGroup(
                        new WaitCommand(100),
                        new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK))
                ));
        //robotBase.turretSubsystem.updatePosition(180);
        robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE);
        robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK);
        DataStorage.alliance = DecodeEnums.Alliance.RED;

        gamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->waitTime += 1000)));

        gamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->waitTime -= 1000)));
    }

    @Override
    public void init_loop() {
        CommandScheduler.getInstance().run();
        gamepad.readButtons();
        telemetry.addData("Wait Time", waitTime);
    }

    @Override
    public void start() {
        follower.setStartingPose(new Pose(startPose.getX(), startPose.getY(), Math.toRadians(-90)));
        CommandScheduler.getInstance().schedule(new WaitCommand(waitTime));
        CommandScheduler.getInstance().schedule(path);
        CommandScheduler.getInstance().schedule(new AutoTurretHeadingCommand(robotBase, follower, DataStorage.redGoalPose));
        CommandScheduler.getInstance().schedule(new DynamicVelocityAutoCommand(robotBase, follower));
        robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE);
        timer.reset();
    }

    @Override
    public void loop() {
        follower.update();
        //robotBase.hoodSubsystem.setDynamicPosition(follower.getPose().distanceFrom(goalPose));

        telemetry.addData("X: ", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Turret Heading Target", robotBase.turretSubsystem.degreeModulus);
        CommandScheduler.getInstance().run();
    }
    @Override
    public void stop(){
        Pose endPose = new Pose(follower.getPose().getX(), follower.getPose().getY(), follower.getPose().getHeading());
        DataStorage.endPosition = endPose;
        DataStorage.alliance = DecodeEnums.Alliance.RED;
    }
}

