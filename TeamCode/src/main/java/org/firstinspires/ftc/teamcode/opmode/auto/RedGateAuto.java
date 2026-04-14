package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;

@Autonomous(name = "Red Gate Auto")
public class RedGateAuto extends OpMode {

    Follower follower;
    RobotBase robotBase;
    SequentialCommandGroup path;

    Pose startPose = new Pose(125, 131, Math.toRadians(-135));

    BezierLine startToLaunch = new BezierLine(
            new Pose(125.000, 131.000),
            new Pose(85.000, 83.500));

    BezierCurve preIntakeSecondRow = new BezierCurve(
            new Pose(85.000, 83.500),
            new Pose(89.054, 68.888),
            new Pose(103.425, 60.000));

    BezierLine intakeSecondRow =  new BezierLine(
            new Pose(103.425, 60.000),
            new Pose(133.000, 60.000));

    PathChain startLaunchAndIntakeSecondRow;
    PathChain intakeSecondRowPath;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);
        robotBase = new RobotBase(hardwareMap);

        startLaunchAndIntakeSecondRow = follower.pathBuilder()
                .addPath(startToLaunch)
                .setTangentHeadingInterpolation()
                //.addParametricCallback(0.97, ()->CommandScheduler.getInstance().schedule(new WaitCommand(1000)))
                .build();

        intakeSecondRowPath = follower.pathBuilder()
                .addPath(preIntakeSecondRow)
                .setLinearHeadingInterpolation(Math.toRadians(-135), Math.toRadians(0))
                .addPath(intakeSecondRow)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        path = new SequentialCommandGroup(
          new FollowPathCommand(follower, startLaunchAndIntakeSecondRow, true, 1)
          //new FollowPathCommand(follower, intakeSecondRowPath, true, 1)
        );
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
        CommandScheduler.getInstance().schedule(path);
    }

    @Override
    public void loop() {
        follower.update();
        CommandScheduler.getInstance().run();

        telemetry.addData("X: ", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
    }
}
