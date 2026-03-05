package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

@Autonomous(name = "Blue Lots of Artifacts")
public class BlueLotsOfArtifactsAuto extends OpMode {
    RobotBase robotBase;
    Follower follower;
    Pose startPose = new Pose(109.8, 134, Math.toRadians(180)).mirror();
    Pose beginLaunch = new Pose(100, 110, Math.toRadians(45)).mirror();
    Pose launchPose = new Pose(87, 87, Math.toRadians(45)).mirror();
    //Pose launchAftPreloadPose = new Pose(85, 85, Math.toRadians(45));
    Pose intakeMiddleLineUp = new Pose(94, 60, Math.toRadians(0)).mirror();
    Pose intakeMiddleRow = new Pose(129, 60, Math.toRadians(0)).mirror();
    Pose pushGate = new Pose(120, 66, Math.toRadians(0)).mirror();
    Pose intakeArtifactsFromGate = new Pose(128, 62, Math.toRadians(32)).mirror();
    Pose launchAftIntakeFromGate = new Pose(89,76,Math.toRadians(42)).mirror();
    Pose facingGoalPoint = new Pose(132, 136/*133.5, 139*/).mirror();
    Pose topRowLineUp = new Pose(96, 82, Math.toRadians(0)).mirror();
    Pose intakeTopRow = new Pose(121, 82).mirror();
    Pose secondToLastLaunch = new Pose(88,77,Math.toRadians(41)).mirror();
    Pose lastLaunch = new Pose(85,82,Math.toRadians(40)).mirror();
    Pose park = new Pose(105,65,Math.toRadians(24)).mirror();

    double dblLaunchVel = 1700;

    PathChain startPath;
    PathChain launchPath;
    PathChain intakeMiddleRowPathLineUp;
    PathChain intakeMiddleRowPath;
    PathChain middleRowToLaunch;
    PathChain launchMiddleToIntake;
    PathChain intakeFromGateToLaunch;
    PathChain intakeTopRowPath;
    PathChain launchTopRow;
    PathChain lastLaunchFromGate;
    PathChain parking;

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
                .setConstantHeadingInterpolation(beginLaunch.getHeading())
                .addParametricCallback(0, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.98, ()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addParametricCallback(0.98, ()->robotBase.intakeSubsystem.intake(-1))
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
                .addPath(new BezierLine(intakeMiddleRow, launchAftIntakeFromGate))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.2, HeadingInterpolator.constant(intakeMiddleRow.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.2, 0.4, HeadingInterpolator.linear(intakeArtifactsFromGate.getHeading(), launchAftIntakeFromGate.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.4, 1, HeadingInterpolator.constant(launchAftIntakeFromGate.getHeading()))))
                //.addParametricCallback(0, ()->new SetAllVelocityCommandGroup(robotBase, 1900))
                .addParametricCallback(0.95, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.95, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.95, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.95,()->robotBase.intakeSubsystem.intake(1))
                .build();


        launchMiddleToIntake = follower.pathBuilder()
                .addPath(new BezierLine(launchAftIntakeFromGate, pushGate))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.5, HeadingInterpolator.linear(launchAftIntakeFromGate.getHeading(), pushGate.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.5, 1, HeadingInterpolator.constant(pushGate.getHeading()))
                ))
                .addParametricCallback(0.5,()->robotBase.intakeSubsystem.intake(-1))
                .addPath(new BezierLine(pushGate, intakeArtifactsFromGate))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.5, HeadingInterpolator.linear(pushGate.getHeading(), intakeArtifactsFromGate.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.5, 1, HeadingInterpolator.constant(intakeArtifactsFromGate.getHeading()))
                ))
                .setConstantHeadingInterpolation(intakeArtifactsFromGate.getHeading())
                .addParametricCallback(0.1, ()-> CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .build();

        intakeFromGateToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(intakeArtifactsFromGate,launchAftIntakeFromGate))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.25, HeadingInterpolator.linear(intakeArtifactsFromGate.getHeading(), launchAftIntakeFromGate.getHeading())),
                        //new HeadingInterpolator.PiecewiseNode(0.25, 1, HeadingInterpolator.facingPoint(facingGoalPoint))))
                        new HeadingInterpolator.PiecewiseNode(0.25, 1, HeadingInterpolator.constant(launchAftIntakeFromGate.getHeading()))))
                //.setLinearHeadingInterpolation(intakeArtifactsFromGate.getHeading(),launchAftIntakeFromGate.getHeading())
                .addParametricCallback(0.1, ()-> CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addParametricCallback(0.97, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.97, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.97, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.8, ()->robotBase.intakeSubsystem.intake(1))
                .build();
        lastLaunchFromGate = follower.pathBuilder()
                .addPath(new BezierLine(intakeArtifactsFromGate,secondToLastLaunch))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.25, HeadingInterpolator.linear(intakeArtifactsFromGate.getHeading(), secondToLastLaunch.getHeading())),
                        //new HeadingInterpolator.PiecewiseNode(0.25, 1, HeadingInterpolator.facingPoint(facingGoalPoint))))
                        new HeadingInterpolator.PiecewiseNode(0.25, 1, HeadingInterpolator.constant(secondToLastLaunch.getHeading()))))
                //.setLinearHeadingInterpolation(intakeArtifactsFromGate.getHeading(),launchAftIntakeFromGate.getHeading())
                .addParametricCallback(0.1, ()-> CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addParametricCallback(0.97, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.97, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.97, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.8, ()->robotBase.intakeSubsystem.intake(1))
                .build();

        intakeTopRowPath = follower.pathBuilder()
                .addPath(new BezierLine(launchAftIntakeFromGate, topRowLineUp))
                .setLinearHeadingInterpolation(launchAftIntakeFromGate.getHeading(), topRowLineUp.getHeading())
                .addParametricCallback(0.1, ()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addPath(new BezierLine(topRowLineUp, intakeTopRow))
                .setConstantHeadingInterpolation(topRowLineUp.getHeading())
                .addParametricCallback(0,()->robotBase.intakeSubsystem.intake(-1))
                .build();

        launchTopRow = follower.pathBuilder()
                .addPath(new BezierLine(topRowLineUp, lastLaunch))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.25, HeadingInterpolator.linear(topRowLineUp.getHeading(), launchAftIntakeFromGate.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.25, 1, HeadingInterpolator.facingPoint(facingGoalPoint))))
                .addParametricCallback(0.95, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.95, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.95, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.95,()->robotBase.intakeSubsystem.intake(1))
                .build();
        parking = follower.pathBuilder()
                .addPath(new BezierLine(launchAftIntakeFromGate,park))
                .setConstantHeadingInterpolation(park.getHeading())
                .build();

        route = new SequentialCommandGroup(
                new InstantCommand(()->dblLaunchVel = 1880),
                new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE)),
                new FollowPathCommand(follower, startPath, true, 1),
                //new WaitUntilCommand(()->!follower.isBusy()),
                //new WaitCommand(250),
                //new FollowPath(follower, launchPath, true, 1),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPathCommand(follower, intakeMiddleRowPathLineUp, true, 1),

                new InstantCommand(()->dblLaunchVel = 1860),
                new FollowPathCommand(follower, intakeMiddleRowPath, true, 1),
                new FollowPathCommand(follower, middleRowToLaunch, true, 1),
                new WaitCommand(250),
                new TransferResetCommandGroup(robotBase),
                new FollowPathCommand(follower, launchMiddleToIntake,true,1),
                new WaitCommand(425),
                new FollowPathCommand(follower, intakeFromGateToLaunch, true, 1),
                new WaitCommand(250),
                new FollowPathCommand(follower, launchMiddleToIntake, true, 1),
                new WaitCommand(425),
                new FollowPathCommand(follower, intakeFromGateToLaunch, true, 1),
                new WaitCommand(250),
                new FollowPathCommand(follower, launchMiddleToIntake, true, 1),
                new WaitCommand(425),
                new FollowPathCommand(follower, lastLaunchFromGate, true, 1),
                new WaitCommand(250),
                new InstantCommand(()->dblLaunchVel = 1830),
                new FollowPathCommand(follower, intakeTopRowPath, true, 1),
                new FollowPathCommand(follower, launchTopRow, true, 1),
                new WaitCommand(250),
                new FollowPathCommand(follower,parking,false,1),


                new TransferResetCommandGroup(robotBase)

        );
        /*new Trigger(()->follower.getCurrentPathChain() == startPath)
                .whileActiveContinuous(new SetAllLaunchVelocityCommandGroup(robotBase, robotBase.limelightSubsystem.getHorizontalDistance(follower)));*/
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
        CommandScheduler.getInstance().schedule(
                new SetAllVelocityCommandGroup(robotBase, dblLaunchVel)
        );
        follower.update();
        telemetry.addData("Launch Velocity", robotBase.launcherSubsystemLeft.getVelocity());
        telemetry.addData("Limelight Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower));
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading: ", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }
    @Override
    public void stop(){
        robotBase.limelightSubsystem.limelight.stop();
        DataStorage.endPosition = follower.getPose();
        DataStorage.alliance = DecodeEnums.Alliance.BLUE;
    }
}
