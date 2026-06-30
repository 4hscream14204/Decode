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
    SequentialCommandGroup path;

    Pose startPose = new Pose(166, 170 ,Math.toRadians(213));
    Pose endPose = new Pose(0,0,Math.toRadians(0));

    BezierLine preloadLaunch = new BezierLine(
            startPose,
            new Pose(126, 125)
    );
//(47)
    BezierCurve intakeRow = new BezierCurve(
            new Pose(126,125),
            new Pose(142,107),
            new Pose(172,107));
//(47)(0)(0)
    BezierLine intakeToLaunch = new BezierLine(
            new Pose(172,107),
            new Pose(126,125));
 //(0)(47)
    BezierLine launchRowToGate = new BezierLine(
            new Pose(126,125),
            new Pose(174,105));
//(47)(30)
    BezierLine gateToLaunch = new BezierLine(
            new Pose(174,105),
            new Pose(126,125));
//(30)(47)
    BezierLine launchToGate2 = new BezierLine(
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

    BezierLine launchToGate4 = new BezierLine(
            new Pose(126,125),
            new Pose(174,105));

    BezierLine gateToLaunch4 = new BezierLine(
            new Pose(174,105),
            new Pose(126,125));

    BezierLine launchToPark = new BezierLine(
            new Pose( 126,125),
            endPose
    );
    PathChain startToLaunch;
    PathChain intakeFirstRow;
    PathChain launchFirstRow;
    PathChain gateCycle;
    //Gate to launch
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
                .addParametricCallback(0.75,()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0))))
                .build();

        intakeFirstRow = follower.pathBuilder()
                .addPath(intakeRow)
                .setLinearHeadingInterpolation(Math.toRadians(47),Math.toRadians(0))
                .build();

        path = new SequentialCommandGroup(

        );
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