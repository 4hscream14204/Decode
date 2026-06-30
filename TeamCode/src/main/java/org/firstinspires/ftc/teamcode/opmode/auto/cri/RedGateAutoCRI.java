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
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;

@Autonomous(name="Red Gate Auto CRI")
public class RedGateAutoCRI extends OpMode {
    Follower follower;
    RobotBase robotBase;
    int artifactsInBotCount;
    SequentialCommandGroup path;

    Pose startPose = new Pose(166, 170 ,Math.toRadians(213));
    Pose endPose = new Pose(172,130,Math.toRadians(90));

    BezierLine preloadLaunch = new BezierLine(
            startPose,
            new Pose(126, 125)
    );
//(0)
    BezierCurve intakeRow = new BezierCurve(
            new Pose(126,125),
            new Pose(142,107),
            new Pose(172,107));
//(0)(0)(0)
    BezierLine intakeToLaunch = new BezierLine(
            new Pose(172,107),
            new Pose(126,125));
 //(0)(0)
    BezierLine launchToGate = new BezierLine(
            new Pose(126,125),
            new Pose(174,105));
//(0)(30)
    BezierLine gateToLaunch = new BezierLine(
            new Pose(174,105),
            new Pose(126,125));
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
    BezierLine launchToPark = new BezierLine(
            new Pose( 126,125),
            endPose
);


    PathChain startToLaunch;
    PathChain intakeFirstRow;
    PathChain launchFirstRow;
    PathChain launchGate;
    PathChain gateLaunch;
    PathChain parking;



    @Override
    public void init(){
        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);
        robotBase = new RobotBase(hardwareMap);
        DataStorage.alliance = DecodeEnums.Alliance.RED;
        robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK);
        robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE);


        startToLaunch = follower.pathBuilder()
                .addPath(preloadLaunch)
                .setLinearHeadingInterpolation(startPose.getHeading(),Math.toRadians(0))
                .addParametricCallback(0.25,()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.3))))
                .addParametricCallback(0.75,()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer())))
                .build();

        intakeFirstRow = follower.pathBuilder()
                .addPath(intakeRow)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        launchFirstRow = follower.pathBuilder()
                .addPath(intakeToLaunch)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addParametricCallback(0.5,()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer())))
                .build();

        launchGate = follower.pathBuilder()
                .addPath(launchToGate)
                .setLinearHeadingInterpolation(Math.toRadians(0),Math.toRadians(30))
                .build();

        gateLaunch = follower.pathBuilder()
                .addPath(gateToLaunch)
                .setLinearHeadingInterpolation(Math.toRadians(30),Math.toRadians(0))
                .addParametricCallback(0.5,()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer())))
                .build();

        parking = follower.pathBuilder()
                .addPath(launchToPark)
                .setLinearHeadingInterpolation(Math.toRadians(0),Math.toRadians(90))
                .build();


        path = new SequentialCommandGroup(
                new FollowPathCommand(follower, startToLaunch,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(300),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->artifactsInBotCount = 0),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.75)),
                new FollowPathCommand(follower, intakeFirstRow, true,1),
                new FollowPathCommand(follower,launchFirstRow,true,1),
                        new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(200),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(300),
                new InstantCommand(()->artifactsInBotCount = 0),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.75)),
                new FollowPathCommand(follower,launchGate,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount = 3).withTimeout(700),
                new FollowPathCommand(follower,gateLaunch,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(200),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(300),
                new InstantCommand(()->artifactsInBotCount = 0),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.75)),
                new FollowPathCommand(follower,launchGate,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount = 3).withTimeout(700),
                new FollowPathCommand(follower,gateLaunch,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(200),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(300),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.75)),
                new InstantCommand(()->artifactsInBotCount = 0),
                new FollowPathCommand(follower,launchGate,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount = 3).withTimeout(700),
                new FollowPathCommand(follower,gateLaunch,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(200),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(300),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.75)),
                new InstantCommand(()->artifactsInBotCount = 0),
                new FollowPathCommand(follower,launchGate,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount = 3).withTimeout(700),
                new FollowPathCommand(follower,gateLaunch,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(200),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(300),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.75)),
                new InstantCommand(()->artifactsInBotCount = 0),
                new FollowPathCommand(follower,launchGate,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount = 3).withTimeout(700),
                new FollowPathCommand(follower,gateLaunch,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new WaitCommand(200),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(300),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.75)),
                new InstantCommand(()->artifactsInBotCount = 0),
                new FollowPathCommand(follower,parking,false,1)

        ));
    }

    @Override
    public void start(){
        CommandScheduler.getInstance().schedule(path);
        follower.setStartingPose(startPose);
    }

    @Override
    public void loop(){
        follower.update();
        telemetry.addData("Y:",follower.getPose().getY());
        telemetry.addData("X:",follower.getPose().getX());
        telemetry.addData("Heading:",Math.toRadians(follower.getPose().getHeading()));
    }

    @Override
    public void stop(){
        DataStorage.endPosition = endPose;
    }

}