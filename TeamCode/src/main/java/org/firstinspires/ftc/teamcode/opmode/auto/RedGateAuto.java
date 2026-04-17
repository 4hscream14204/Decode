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
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.commands.DynamicVelocityCommand;
import org.firstinspires.ftc.teamcode.commands.TransferCommand;
import org.firstinspires.ftc.teamcode.commands.TurretHeadingControlCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;

@Autonomous(name = "Red Gate Auto")
public class RedGateAuto extends OpMode {

    Follower follower;
    RobotBase robotBase;
    SequentialCommandGroup path;

    Pose startPose = new Pose(125, 131, Math.toRadians(-135));

    BezierLine startToLaunch = new BezierLine(startPose, new Pose(91.000, 83.500, Math.toRadians(-135)));

    BezierCurve preIntakeSecondRow = new BezierCurve(
            new Pose(88.000, 83.500),
            new Pose(89.054, 68.888),
            new Pose(103.425, 60.000));

    BezierLine intakeSecondRow =  new BezierLine(
            new Pose(103.425, 60.000),
            new Pose(128.000, 60.000));

    BezierLine secondRowToLaunch = new BezierLine(
            new Pose(128, 60),
            new Pose(90, 83));

    BezierLine launchToGateLine = new BezierLine(
            new Pose(88, 83),
            new Pose(135, 63, Math.toRadians(37)));

    BezierLine gateToLaunchLine = new BezierLine(
            new Pose(135, 64), new Pose(85, 83));

    BezierLine preIntakeFirstRow = new BezierLine(
            new Pose(135,64),
            new Pose(107,84));

    BezierLine intakeFirstRow = new BezierLine(
            new Pose(107,84),
            new Pose(122,84));

    BezierLine firstRowToLaunch = new BezierLine(
            new Pose(122,84),
            new Pose(88,113));


            PathChain startLaunchAndIntakeSecondRow;
    PathChain preIntakeSecondRowPath;
    PathChain intakeSecondRowPath;
    PathChain launchToGate;
    PathChain gateToLaunch;
    PathChain preIntakeFirstRowPath;
    PathChain intakeFirstRowPath;


    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);
        robotBase = new RobotBase(hardwareMap);

        startLaunchAndIntakeSecondRow = follower.pathBuilder()
                .addPath(startToLaunch)
                .setConstantHeadingInterpolation(startPose.getHeading())
                //.addParametricCallback(0.97, ()->CommandScheduler.getInstance().schedule(new WaitCommand(1000)))
                .build();

        preIntakeSecondRowPath = follower.pathBuilder()
                .addPath(preIntakeSecondRow)
                .setLinearHeadingInterpolation(Math.toRadians(-135), Math.toRadians(0), 0.8)
                .addPath(intakeSecondRow)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
        intakeSecondRowPath = follower.pathBuilder()
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
        intakeFirstRowPath = follower.pathBuilder()
                .addPath(firstRowToLaunch)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        path = new SequentialCommandGroup(
            new InstantCommand(()->robotBase.hoodSubsystem.close()),
            //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1740)),
            new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
            new FollowPathCommand(follower, startLaunchAndIntakeSecondRow, true, 1),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
            new WaitCommand(500),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            //new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(-1)),
            new FollowPathCommand(follower, preIntakeSecondRowPath, true, 1),
            new FollowPathCommand(follower, intakeSecondRowPath,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(500),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            //new TransferCommand(robotBase, follower),
            //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1000)),
            //new WaitCommand(1000),
            new FollowPathCommand(follower, launchToGate, true, 1),
            new WaitCommand(250),
            new FollowPathCommand(follower, gateToLaunch, true, 1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(500),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            //new TransferCommand(robotBase, follower),
            //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1000)),
            //new WaitCommand(500),
            new FollowPathCommand(follower, launchToGate, true, 1),
            new WaitCommand(250),
            new FollowPathCommand(follower, gateToLaunch, true, 1),
            //new TransferCommand(robotBase, follower),
                //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1000)),
            new WaitCommand(500)
           // new FollowPathCommand(follower,launchToGate,true,1),
          //  new WaitCommand(250),
            //new FollowPathCommand(follower,gateToLaunch,true,1),
            //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1000)),
            //new WaitCommand(500),
            //new FollowPathCommand(follower,launchToGate,true,1),
            //new WaitCommand(250),
            //new FollowPathCommand(follower, gateToLaunch,true,1),
            //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1000)),
            //new WaitCommand(500),
            //new FollowPathCommand(follower,launchToGate,true,1),
            //new WaitCommand(250),
            //new FollowPathCommand(follower,gateToLaunch,true,1),
            //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1000)),
            //new WaitCommand(500),
            //new FollowPathCommand(follower,preIntakeFirstRowPath,true,1),
            //new FollowPathCommand(follower,intakeFirstRowPath,true,1)

          //new FollowPathCommand(follower, intakeSecondRowPath, true, 1)
                );
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
