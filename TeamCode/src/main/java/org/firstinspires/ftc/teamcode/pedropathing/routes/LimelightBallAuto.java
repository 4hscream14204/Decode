package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.teamcode.pedropathing.poses.PoseGenerator;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

@Autonomous(name = "Limelight Ball")
public class LimelightBallAuto extends OpMode {
    Follower follower;
    RobotBase robotBase;
    PoseGenerator poseGenerator;
    SequentialCommandGroup command;
    PathChain goToBall;
    PathChain grabNextBall;
    double limelightOffset;
    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        robotBase = new RobotBase(hardwareMap);
        poseGenerator = new PoseGenerator(robotBase.limelightSubsystem, follower);
        robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.PURPLEARTIFACT);
        robotBase.limelightSubsystem.updateLimelight();
        follower.setStartingPose(new Pose(0, 0, Math.toRadians(0)));
    }

    @Override
    public void init_loop() {
        poseGenerator.calculatePose();
        robotBase.limelightSubsystem.updateLimelight();

        telemetry.addData("Ball Pose X", poseGenerator.calculatePose().getX());
        telemetry.addData("Ball Pose Y", poseGenerator.calculatePose().getY());
        telemetry.addData("Heading", follower.getHeading());
    }

    @Override
    public void start() {

        robotBase.limelightSubsystem.updateLimelight();

        follower.setStartingPose(new Pose(0, 0, Math.toRadians(0)));

        goToBall = follower.pathBuilder()
                .addPath(new BezierLine(new Pose(0, 0, Math.toRadians(0)), poseGenerator.calculatePose()))
                .setConstantHeadingInterpolation(follower.getHeading() - Math.toRadians(robotBase.limelightSubsystem.getTargetX()))
                .build();

        grabNextBall = follower.pathBuilder()
                .addPath(new Path(new BezierLine(follower::getPose, poseGenerator.calculatePose())))
                .setHeadingInterpolation(HeadingInterpolator.constant(follower.getHeading() - Math.toRadians(robotBase.limelightSubsystem.getTargetX())))
                .build();

        command = new SequentialCommandGroup(
                new FollowPath(follower, goToBall, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new InstantCommand(()->follower.turnDegrees(90, false)),
                new FollowPath(follower, grabNextBall, true, 1)
        );
        command.schedule();
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        robotBase.limelightSubsystem.updateLimelight();
        follower.update();
        robotBase.intakeSubsystem.intake(-1);

        telemetry.addData("Ball Pose X", poseGenerator.calculatePose().getX());
        telemetry.addData("Ball Pose Y", poseGenerator.calculatePose().getY());
        telemetry.addData("Heading", follower.getHeading());
    }
}
