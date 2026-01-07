package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.pedropathing.paths.HeadingInterpolator;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllLaunchVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
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
    //Pose launchAftPreloadPose = new Pose(85, 85, Math.toRadians(45));
    Pose intakeMiddleLineUp = new Pose(98, 60, Math.toRadians(0));
    Pose intakeMiddleRow = new Pose(130, 60, Math.toRadians(0));
    Pose intakeArtifactsFromGate = new Pose(129, 61, Math.toRadians(34.59));
    Pose launchAftIntakeFromGate = new Pose(85,77,Math.toRadians(45));
    Pose facingGoalPoint = new Pose(132, 136/*133.5, 139*/);
    Pose topRowLineUp = new Pose(99, 83.5, Math.toRadians(0));
    Pose intakeTopRow = new Pose(124, 83.57);

    PathChain startPath;
    PathChain launchPath;
    PathChain intakeMiddleRowPathLineUp;
    PathChain intakeMiddleRowPath;
    PathChain middleRowToLaunch;
    PathChain launchMiddleToIntake;
    PathChain intakeFromGateToLaunch;
    PathChain intakeTopRowPath;
    PathChain launchTopRow;

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
                .addPath(new BezierLine(beginLaunch, launchPose))
                .setConstantHeadingInterpolation(Math.toRadians(47))
                .addParametricCallback(0.25, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(1, ()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addParametricCallback(1, ()->robotBase.intakeSubsystem.intake(-1))
                .build();
        /*launchPath = follower.pathBuilder()
                .addPath(new BezierLine(beginLaunch, launchPose))
                .setConstantHeadingInterpolation(Math.toRadians(47))
                .addParametricCallback(0.25, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(1, ()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addParametricCallback(1, ()->robotBase.intakeSubsystem.intake(-1))
                .build();*/

        intakeMiddleRowPathLineUp = follower.pathBuilder()
                .addPath(new BezierLine(launchPose, intakeMiddleLineUp))
                .setLinearHeadingInterpolation(launchPose.getHeading(), intakeMiddleLineUp.getHeading())
                .build();

        intakeMiddleRowPath = follower.pathBuilder()
                .addPath(new BezierLine(intakeMiddleLineUp, intakeMiddleRow))
                .setConstantHeadingInterpolation(intakeMiddleRow.getHeading())
                .build();

        middleRowToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(intakeMiddleRow, launchAftIntakeFromGate))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.5, HeadingInterpolator.linear(intakeArtifactsFromGate.getHeading(), launchAftIntakeFromGate.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.5, 1, HeadingInterpolator.facingPoint(facingGoalPoint))))
                .addParametricCallback(0, ()->new SetAllVelocityCommandGroup(robotBase, 1900))
                .addParametricCallback(0.85, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.85, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.85, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .build();
        launchMiddleToIntake = follower.pathBuilder()
                .addPath(new BezierLine(launchAftIntakeFromGate, intakeArtifactsFromGate))
                .setConstantHeadingInterpolation(intakeArtifactsFromGate.getHeading())
                .addParametricCallback(0.1, ()-> CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .build();
        intakeFromGateToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(intakeArtifactsFromGate,launchAftIntakeFromGate))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.5, HeadingInterpolator.linear(intakeArtifactsFromGate.getHeading(), launchAftIntakeFromGate.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.5, 1, HeadingInterpolator.facingPoint(facingGoalPoint))))
                //.setLinearHeadingInterpolation(intakeArtifactsFromGate.getHeading(),launchAftIntakeFromGate.getHeading())
                .addParametricCallback(0.1, ()-> CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addParametricCallback(0.85, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.85, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.85, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .build();

        intakeTopRowPath = follower.pathBuilder()
                .addPath(new BezierLine(launchAftIntakeFromGate, topRowLineUp))
                .setLinearHeadingInterpolation(launchAftIntakeFromGate.getHeading(), topRowLineUp.getHeading())
                .addParametricCallback(0.1, ()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addPath(new BezierLine(topRowLineUp, intakeTopRow))
                .setConstantHeadingInterpolation(topRowLineUp.getHeading())
                .build();

        launchTopRow = follower.pathBuilder()
                .addPath(new BezierLine(topRowLineUp, launchAftIntakeFromGate))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.5, HeadingInterpolator.linear(topRowLineUp.getHeading(), launchAftIntakeFromGate.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.5, 1, HeadingInterpolator.facingPoint(facingGoalPoint))))
                .addParametricCallback(0.85, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.85, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.85, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .build();

        route = new SequentialCommandGroup(
                new FollowPathCommand(follower, startPath, false, 1),
                //new WaitUntilCommand(()->!follower.isBusy()),
                //new WaitCommand(250),
                //new FollowPath(follower, launchPath, true, 1),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPathCommand(follower, intakeMiddleRowPathLineUp, true, 1),
                new FollowPathCommand(follower, intakeMiddleRowPath, true, 1),
                new FollowPathCommand(follower, middleRowToLaunch, true, 1),
                new WaitCommand(250),
                new TransferResetCommandGroup(robotBase),
                new FollowPathCommand(follower, launchMiddleToIntake,true,1),
                new WaitCommand(1750),
                new FollowPathCommand(follower, intakeFromGateToLaunch, true, 1),
                new WaitCommand(250),
                new FollowPathCommand(follower, launchMiddleToIntake, true, 1),
                new WaitCommand(1750),
                new FollowPathCommand(follower, intakeFromGateToLaunch, true, 1),
                new WaitCommand(250),
                new FollowPathCommand(follower, launchMiddleToIntake, true, 1),
                new WaitCommand(1750),
                new FollowPathCommand(follower, intakeFromGateToLaunch, true, 1),
                new WaitCommand(250),
                new FollowPathCommand(follower, intakeTopRowPath, true, 1),
                new FollowPathCommand(follower, launchTopRow, true, 1)
        );
        new Trigger(()->follower.getCurrentPathChain() == startPath)
                .whileActiveContinuous(new SetAllLaunchVelocityCommandGroup(robotBase, robotBase.limelightSubsystem.getHorizontalDistance(follower)));
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
        telemetry.addData("Launch Velocity", robotBase.launcherSubsystemLeft.getVelocity());
        telemetry.addData("Limelight Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower));
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading: ", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }
}
