package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.commands.DynamicVelocityCommand;
import org.firstinspires.ftc.teamcode.commands.TurretHeadingControlCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;

@Autonomous(name = "Red Gate Auto")
public class RedGateAuto extends OpMode {

    Follower follower;
    RobotBase robotBase;
    SequentialCommandGroup path;

    Pose startPose = new Pose(125, 131, Math.toRadians(-135));

    BezierLine startToLaunch = new BezierLine(startPose, new Pose(83.000, 83.500, Math.toRadians(-135)));

    BezierCurve preIntakeSecondRow = new BezierCurve(
            new Pose(88.000, 83.500),
            new Pose(89.054, 68.888),
            new Pose(108.000, 63.000));

    BezierLine intakeSecondRow =  new BezierLine(
            new Pose(108.000, 63.000),
            new Pose(128.000, 63.000));

    BezierLine secondRowToLaunch = new BezierLine(
            new Pose(128, 63),
            new Pose(90, 83));

    BezierLine launchToGateLine = new BezierLine(
            new Pose(85, 85),
            new Pose(135, 62, Math.toRadians(37)));

    BezierLine gateToLaunchLine = new BezierLine(
            new Pose(135, 62), new Pose(85, 85));

    BezierLine preIntakeFirstRow = new BezierLine(
            new Pose(85,85),
            new Pose(107,84));

    BezierLine intakeFirstRow = new BezierLine(
            new Pose(107,84),
            new Pose(127,84));

    BezierLine firstRowToLaunch = new BezierLine(
            new Pose(127,84),
            new Pose(88,113));


    PathChain startLaunch;
    PathChain intakeSecondRowPath;
    PathChain launchSecondRowPath;
    PathChain launchToGate;
    PathChain gateToLaunch;
    PathChain preIntakeFirstRowPath;
    PathChain launchFirstRowPath;


    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);
        robotBase = new RobotBase(hardwareMap);

        startLaunch = follower.pathBuilder()
                .addPath(startToLaunch)
                .setConstantHeadingInterpolation(startPose.getHeading())
                //.addParametricCallback(0.97, ()->CommandScheduler.getInstance().schedule(new WaitCommand(1000)))
                .build();

        intakeSecondRowPath = follower.pathBuilder()
                .addPath(preIntakeSecondRow)
                .setLinearHeadingInterpolation(Math.toRadians(-135), Math.toRadians(0), 0.8)
                .addPath(intakeSecondRow)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
        launchSecondRowPath = follower.pathBuilder()
                .addPath(secondRowToLaunch)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        launchToGate = follower.pathBuilder()
                .addPath(launchToGateLine)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(37))
                .build();

        gateToLaunch = follower.pathBuilder()
                .addPath(gateToLaunchLine)
                .setLinearHeadingInterpolation(Math.toRadians(35), Math.toRadians(0))
                .build();

        preIntakeFirstRowPath = follower.pathBuilder()
                .addPath(preIntakeFirstRow)
                .setLinearHeadingInterpolation(Math.toRadians(-135),Math.toRadians(0), 0.8)
                .addPath(intakeFirstRow)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
        launchFirstRowPath = follower.pathBuilder()
                .addPath(firstRowToLaunch)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        path = new SequentialCommandGroup(
            new InstantCommand(()->robotBase.hoodSubsystem.close()),
            //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1740)),
            new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
            new FollowPathCommand(follower, startLaunch, true, 1),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
            new WaitCommand(500),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            new FollowPathCommand(follower, intakeSecondRowPath, true, 1),
            new FollowPathCommand(follower, launchSecondRowPath,true,1),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
            new WaitCommand(500),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            //new TransferCommand(robotBase, follower),
            //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1000)),
            //new WaitCommand(1000),
            new FollowPathCommand(follower, launchToGate, true, 1),
            new WaitCommand(700),
            new FollowPathCommand(follower, gateToLaunch, true, 1),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
            new WaitCommand(500),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            //new TransferCommand(robotBase, follower),
            //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1000)),
            //new WaitCommand(500),
            new FollowPathCommand(follower, launchToGate, false, 1),
            new WaitCommand(700),
            new FollowPathCommand(follower, gateToLaunch, true, 1),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
            new WaitCommand(500),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            new FollowPathCommand(follower,launchToGate,true,1),
            new WaitCommand(700),
            new FollowPathCommand(follower,gateToLaunch,true,1),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
            new WaitCommand(500),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            new FollowPathCommand(follower,launchToGate,true,1),
            new WaitCommand(700),
            new FollowPathCommand(follower, gateToLaunch,true,1),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
            new WaitCommand(500),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            new FollowPathCommand(follower,launchToGate,true,1),
            new WaitCommand(700),
            new FollowPathCommand(follower,gateToLaunch,true,1),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
            new WaitCommand(500),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            new FollowPathCommand(follower,preIntakeFirstRowPath,true,1),
            new FollowPathCommand(follower,launchFirstRowPath,true,1),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
            new WaitCommand(500),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK))
                    //*/
                );
        //robotBase.turretSubsystem.updatePosition(180);
        robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE);
        robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK);
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
        CommandScheduler.getInstance().schedule(path);
        CommandScheduler.getInstance().schedule(new TurretHeadingControlCommandGroup(robotBase, follower));
        CommandScheduler.getInstance().schedule(new DynamicVelocityCommand(robotBase, follower));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        follower.update();

        telemetry.addData("X: ", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
    }
}
