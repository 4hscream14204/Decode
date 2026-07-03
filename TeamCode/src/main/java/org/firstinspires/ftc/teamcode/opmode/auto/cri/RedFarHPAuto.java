package org.firstinspires.ftc.teamcode.opmode.auto.cri;


import com.pedropathing.follower.Follower;
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

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.commands.TurretHeadingControlCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;

@Autonomous (name = "Red Far HP Auto")
public class RedFarHPAuto extends OpMode {
    RobotBase robotBase;
    SequentialCommandGroup path;
    Follower follower;


    Pose startPose = new Pose(124,8,Math.toRadians(90));
    Pose endPose = new Pose(176,36,Math.toRadians(0));

    BezierLine startToLaunch = new BezierLine(
            startPose,
            new Pose(114,38));

    BezierLine launchToHp = new BezierLine(
            new Pose(114,38),
            new Pose(180,10));

    BezierLine fromHPToLaunch = new BezierLine(
            new Pose(180,10),
            new Pose(114,38));

    BezierLine launchToPark = new BezierLine(
            new Pose(114,38),
            endPose);



    PathChain launchPreload;
    PathChain intakeFromHP;
    PathChain HPToLaunch;
    PathChain Parking;


    @Override
    public void init(){
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        CommandScheduler.getInstance().reset();

        launchPreload = follower.pathBuilder()
                .addPath(startToLaunch)
                .setLinearHeadingInterpolation(Math.toRadians(90),Math.toRadians(0))
                .build();
        intakeFromHP = follower.pathBuilder()
                .addPath(launchToHp)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
        HPToLaunch = follower.pathBuilder()
                .addPath(fromHPToLaunch)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        Parking = follower.pathBuilder()
                .addPath(launchToPark)
                .setLinearHeadingInterpolation(Math.toRadians(0),Math.toRadians(90))
                .build();


        path = new SequentialCommandGroup(
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.4)),
                new FollowPathCommand(follower, launchPreload, true, 1),
                new WaitCommand(800),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(800),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new FollowPathCommand(follower, intakeFromHP, true, 1),
                new FollowPathCommand(follower, HPToLaunch,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(800),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new FollowPathCommand(follower,intakeFromHP,true,1),
                new FollowPathCommand(follower,HPToLaunch,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(800),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new FollowPathCommand(follower,intakeFromHP,true,1),
                new FollowPathCommand(follower,HPToLaunch,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(800),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new FollowPathCommand(follower,intakeFromHP,true,1),
                new FollowPathCommand(follower,HPToLaunch,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(800),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),




                );
    }

    @Override
    public void start(){
        follower.setStartingPose(startPose);
        CommandScheduler.getInstance().schedule(path);
        CommandScheduler.getInstance().schedule(new TurretHeadingControlCommandGroup(robotBase, follower));

    }

    @Override
    public void loop(){
        follower.update();
        telemetry.addData("X: ", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
        CommandScheduler.getInstance().run();

    }

    @Override
    public void stop(){
        Pose endPose = new Pose(follower.getPose().getX(), follower.getPose().getY(), follower.getPose().getHeading());
        DataStorage.endPosition = endPose;
        DataStorage.alliance = DecodeEnums.Alliance.RED;

    }
}
