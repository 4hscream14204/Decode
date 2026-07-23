package org.firstinspires.ftc.teamcode.opmode.auto.cri;

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

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.commands.AutoTurretHeadingCommand;
import org.firstinspires.ftc.teamcode.commands.DynamicVelocityCommand;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;

@Autonomous(name="blue Gate & Spike Auto")
public class BlueGateAuto2Spikes2 extends OpMode {
    Follower follower;
    RobotBase robotBase;
    int artifactsInBotCount;
    SequentialCommandGroup path;
    double fieldWidth = 189.5;

    Pose startPose = new Pose(166, 170 ,Math.toRadians(-135)).mirror(fieldWidth);
    Pose endPose = new Pose(141,162,Math.toRadians(0)).mirror(fieldWidth);

    BezierLine preloadLaunch = new BezierLine(
            startPose,
            new Pose(149, 136).mirror(fieldWidth));
    //(0)
    BezierLine intakeRow = new BezierLine(
            new Pose(140,150).mirror(fieldWidth),
            new Pose(142,100).mirror(fieldWidth));

    BezierLine intakeRowGrabBalls = new BezierLine(
            new Pose(142, 100).mirror(fieldWidth),
            new Pose(177, 100).mirror(fieldWidth)
    );
    //178 103
    BezierLine intakeSecondRowLineUp = new BezierLine(
            new Pose(149, 136).mirror(fieldWidth),
            new Pose(142,80).mirror(fieldWidth));

    BezierLine intakeSecondRow = new BezierLine(
            new Pose(142, 80).mirror(fieldWidth),
            new Pose(177, 80).mirror(fieldWidth)
    );

    //BezierLine intakeSecondRow = new BezierLine(
    //new Pose(135, 79),
    //new Pose(165, 79)
    //);

    BezierLine launchSecondRow = new BezierLine(
            new Pose(180, 80).mirror(fieldWidth),
            new Pose(149, 136).mirror(fieldWidth)
    );
    //(0)(0)(0)
    BezierLine intakeToLaunch = new BezierLine(
            new Pose(173,112).mirror(fieldWidth),
            new Pose(149,136).mirror(fieldWidth));
    //(0)(0
    BezierCurve launchToGate = new BezierCurve(
            new Pose(149,136).mirror(fieldWidth),
            new Pose(148,120).mirror(fieldWidth),
            new Pose(12,100));
    //(0)(30) 179,103,
    BezierCurve gateToLaunch = new BezierCurve(
            new Pose(12, 100),
            new Pose(148,120).mirror(fieldWidth),
            new Pose(149,136).mirror(fieldWidth));
    //81
    BezierLine secondRowMoveBack = new BezierLine(
            new Pose(165, 79),
            new Pose(135, 79)
    );
    //(30)(0)
    //Extra Gate poses
  /*  BezierLine launchToGate2 = new BezierLine(
            new Pose(126,125),
            new Pose(174,105));

    BezierLine gateToLaunch2 = new BezierLine(
            new Pose(174,105),
            new Pose(126,125));

    BezierLine launchToGate3 = new BezierLine(
            new Pose(126,125),
            new Pose(174,105));

    BezierLine gateToLaunch3 = new BezierLine(
            new Pose(174,105),
            new Pose(126,125));

*/
    BezierLine gateToPark = new BezierLine(
            new Pose(35,102).mirror(fieldWidth),
            endPose);


    PathChain startToLaunch;
    PathChain intakeFirstRow;
    PathChain launchFirstRow;
    PathChain launchGate;
    PathChain gateLaunch;
    PathChain parking;
    //PathChain intakeSecondRowLineUpPath;
    PathChain intakeSecondRowLineUpPath;
    PathChain intakeSecondRowPath;
    PathChain launchSecondRowPath;


    @Override
    public void init(){
        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);
        robotBase = new RobotBase(hardwareMap);
        DataStorage.alliance = DecodeEnums.Alliance.BLUE;
        robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK);
        robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE);


        startToLaunch = follower.pathBuilder()
                .addPath(preloadLaunch)
                .setLinearHeadingInterpolation(startPose.getHeading(),Math.toRadians(180))
                .addParametricCallback(0.25,()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.3))))
                .addParametricCallback(0.75,()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer())))
                .build();

        intakeFirstRow = follower.pathBuilder()
                .addPath(intakeRow)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        PathChain intakeFirstRowGrabBallsPath = follower.pathBuilder()
                .addPath(intakeRowGrabBalls)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        PathChain hitGate = follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(177, 100).mirror(fieldWidth), new Pose(158, 112).mirror(fieldWidth), new Pose(172, 108).mirror(fieldWidth)))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
//176
        launchFirstRow = follower.pathBuilder()
                .addPath(intakeToLaunch)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addParametricCallback(0.5,()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer())))
                .build();

        //   intakeSecondRowLineUpPath = follower.pathBuilder()
        //         .addPath(intakeSecondRowLineUp)
        //       .setConstantHeadingInterpolation(Math.toRadians(0))
        //     .build();

        intakeSecondRowLineUpPath = follower.pathBuilder()
                .addPath(intakeSecondRowLineUp)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        intakeSecondRowPath = follower.pathBuilder()
                .addPath(intakeSecondRow)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        launchSecondRowPath = follower.pathBuilder()
                .addPath(launchSecondRow)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        launchGate = follower.pathBuilder()
                .addPath(launchToGate)
                .setLinearHeadingInterpolation(Math.toRadians(180),Math.toRadians(143))
                .build();

        gateLaunch = follower.pathBuilder()
                .addPath(gateToLaunch)
                .setLinearHeadingInterpolation(Math.toRadians(142),Math.toRadians(180))
                .addParametricCallback(0.5,()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer())))
                .build();

        parking = follower.pathBuilder()
                .addPath(gateToPark)
                .setLinearHeadingInterpolation(Math.toRadians(135),Math.toRadians(180))
                .build();


        path = new SequentialCommandGroup(
                new FollowPathCommand(follower, startToLaunch,true,1),
                new WaitCommand(50),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(100),
                //new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->artifactsInBotCount = 0),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.35)),
                // new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new FollowPathCommand(follower, intakeFirstRow, false,1),
                new FollowPathCommand(follower, intakeFirstRowGrabBallsPath, false, 1),
                new FollowPathCommand(follower, hitGate, false, 1),
                new FollowPathCommand(follower,launchFirstRow,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(50),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(100),
                new InstantCommand(()->artifactsInBotCount = 0),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.35)),
                // new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new FollowPathCommand(follower, intakeSecondRowLineUpPath, false,1),
                new FollowPathCommand(follower, intakeSecondRowPath, false, 1),
                new FollowPathCommand(follower,launchSecondRowPath,true,1),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(50),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(100),
                new InstantCommand(()->artifactsInBotCount = 0),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(-0.75)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.transfer(0.2)),
                new FollowPathCommand(follower,launchGate, true, 1),
                // new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount == 3).withTimeout(900),
                //new WaitCommand(900),
                new FollowPathCommand(follower,gateLaunch,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(50),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(100),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(-0.5)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.transfer(0.2)),
                new InstantCommand(()->artifactsInBotCount = 0),
                /*new FollowPathCommand(follower,launchGate,true,1),
                //  new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount == 3).withTimeout(700),
                // new WaitCommand(900),
                new FollowPathCommand(follower,gateLaunch,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(50),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(100),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(-0.5)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.transfer(0.2)),
                new InstantCommand(()->artifactsInBotCount = 0),*/
                new FollowPathCommand(follower,launchGate,true,1),
                //  new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount == 3).withTimeout(900),
                // new WaitCommand(900),
                new FollowPathCommand(follower,gateLaunch,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(50),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(100),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intake(-0.5)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.transfer(0.2)),
                new InstantCommand(()->artifactsInBotCount = 0),
                new FollowPathCommand(follower,launchGate,true,1),
                //  new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount == 3).withTimeout(900),
                //new WaitCommand(900),
                new FollowPathCommand(follower,parking,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(50),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(100),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.35)),
                new InstantCommand(()->artifactsInBotCount = 0)
        );
    }

    @Override
    public void start(){
        CommandScheduler.getInstance().schedule(path);
        follower.setStartingPose(startPose);
        CommandScheduler.getInstance().schedule(new AutoTurretHeadingCommand(robotBase, follower, DataStorage.blueGoalPose));
        CommandScheduler.getInstance().schedule(new DynamicVelocityCommand(robotBase, follower));
        robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE);
    }

    @Override
    public void loop(){
        follower.update();
        telemetry.addData("Y:",follower.getPose().getY());
        telemetry.addData("X:",follower.getPose().getX());
        telemetry.addData("Heading:",Math.toDegrees(follower.getPose().getHeading()));
        CommandScheduler.getInstance().run();
    }

    @Override
    public void stop(){
        DataStorage.endPosition = endPose;
    }

}