package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.commandgroups.general.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

@Autonomous(name = "Lots of Artifacts")
public class LotsOfArtifactsAuto extends OpMode {
    RobotBase robotBase;
    Follower follower;
    Pose startPose = new Pose(111.62, 135.55, Math.toRadians(180));
    Pose beginLaunch = new Pose(95, 92, Math.toRadians(47));
    Pose launchPose = new Pose(82, 78, Math.toRadians(47));
    Pose launchAftPreloadPose = new Pose(85, 85, Math.toRadians(45));
    Pose intakeMiddleLineUp = new Pose(98, 60, Math.toRadians(0));
    Pose intakeMiddleRow = new Pose(130, 60, Math.toRadians(0));
    Pose intakeArtifactsFromGate = new Pose(129, 61, Math.toRadians(34.59));
    Pose launchAftIntakeFromGate = new Pose(85,85,Math.toRadians(45));

    PathChain startPath;
    PathChain launchPath;
    PathChain intakeMiddleRowPathLineUp;
    PathChain intakeMiddleRowPath;
    PathChain middleRowToLaunch;
    PathChain launchMiddleToIntake;

    SequentialCommandGroup route;
    @Override
    public void init() {
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.OBELISK);
        robotBase.sorterCameraSubsystem.getAnalysis();

        startPath = follower.pathBuilder()
                .addPath(new BezierLine(startPose, beginLaunch))
                .setLinearHeadingInterpolation(startPose.getHeading(), beginLaunch.getHeading())
                .build();
        launchPath = follower.pathBuilder()
                .addPath(new BezierLine(beginLaunch, launchPose))
                .setConstantHeadingInterpolation(Math.toRadians(47))
                .addParametricCallback(0.25, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(1, ()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addParametricCallback(1, ()->robotBase.intakeSubsystem.intake(-1))
                .build();

        intakeMiddleRowPathLineUp = follower.pathBuilder()
                .addPath(new BezierLine(launchPose, intakeMiddleLineUp))
                .setLinearHeadingInterpolation(launchPose.getHeading(), intakeMiddleLineUp.getHeading())
                .build();

        intakeMiddleRowPath = follower.pathBuilder()
                .addPath(new BezierLine(intakeMiddleLineUp, intakeMiddleRow))
                .setConstantHeadingInterpolation(intakeMiddleRow.getHeading())
                .build();

        middleRowToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(intakeMiddleRow, launchAftPreloadPose))
                .setLinearHeadingInterpolation(intakeMiddleRow.getHeading(), launchAftPreloadPose.getHeading())
                .build();
        launchMiddleToIntake = follower.pathBuilder()
                .addPath(new BezierLine(launchAftPreloadPose, intakeArtifactsFromGate))
                .setConstantHeadingInterpolation(intakeArtifactsFromGate.getHeading())
                .build();
        intakeFromGateToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(intakeArtifactsFromGate,launchAftIntakeFromGate))
                .setLinearHeadingInterpolation(intakeArtifactsFromGate.getHeading(),launchAftIntakeFromGate.getHeading())
                .build();



        route = new SequentialCommandGroup(
                new FollowPath(follower, startPath, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new WaitCommand(1000),
                new FollowPath(follower, launchPath, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, intakeMiddleRowPathLineUp, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, intakeMiddleRowPath, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, middleRowToLaunch, false, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new WaitCommand(500),
                new WaitUntilCommand(()->!follower.isBusy()),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, launchMiddleToIntake,false,1)
        );
    }

    @Override
    public void init_loop() {
        robotBase.sorterCameraSubsystem.getAnalysis();
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
        CommandScheduler.getInstance().schedule(route);
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        follower.update();
        robotBase.launcherSubsystemLeft.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower));
        robotBase.launcherSubsystemMiddle.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower));
        robotBase.launcherSubsystemRight.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower));
        telemetry.addData("Launch Velocity", robotBase.launcherSubsystemLeft.getVelocity());
        telemetry.addData("Limelight Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower));
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading: ", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }
}
