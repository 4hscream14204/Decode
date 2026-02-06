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
import org.firstinspires.ftc.teamcode.commandgroups.general.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

@Autonomous(name = "Blue DCS Auto")
public class BlueDCSAuto extends OpMode{
    RobotBase robotBase;
    Follower follower;
    Pose startPose = new Pose(109.8, 134, Math.toRadians(180)).mirror();
    Pose beginLaunch = new Pose(39, 95, Math.toRadians(132));
    Pose launchPose1 = new Pose(43, 84, Math.toRadians(132));
    Pose launchPose2 = new Pose(41,80,Math.toRadians(134));
    Pose launchPose3 = new Pose(41, 80, Math.toRadians(133));
    Pose launchAftIntakeFromGate = new Pose(93,91.5,Math.toRadians(45)).mirror();
    //Pose launchAftPreloadPose = new Pose(85, 85, Math.toRadians(45));
    Pose intakeMiddleLineUp = new Pose(23, 69, Math.toRadians(-90));
    Pose intakeMiddleRow = new Pose(23, 57, Math.toRadians(-90));
    Pose openGateForDCS = new Pose(12, 57, Math.toRadians(-90));
    Pose intakeArtifactsFromGate = new Pose(0.75, 59.6, Math.toRadians(151.3));
    //Pose moveBackFromGateToIntake = new Pose(136, 69, Math.toRadians(47));
    Pose facingGoalPoint = new Pose(138, 136/*133.5, 139*/);
    Pose topRowLineUp = new Pose(30, 78, Math.toRadians(180));
    Pose intakeTopRow = new Pose(5, 78, Math.toRadians(180));
    Pose park = new Pose(116,69.65,Math.toRadians(23.6)).mirror();

    double dblLaunchVel = 2000;

    PathChain startPath;
    PathChain launchPath;
    PathChain intakeTopRowPath;
    PathChain launchTopRow;
    PathChain intakeMiddleRowPathLineUp;
    PathChain intakeMiddleRowPath;
    PathChain middleRowToLaunch;
    PathChain intakeAndOpenGateDCS;

    PathChain launchMiddleToIntake;
    PathChain intakeFromGateToLaunch;
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
                .addPath(new BezierLine(beginLaunch, launchPose1))
                .setConstantHeadingInterpolation(beginLaunch.getHeading())
                .addParametricCallback(0, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                //.addParametricCallback(1, ()-> CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
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

        intakeAndOpenGateDCS = follower.pathBuilder()
                .addPath(new BezierLine(launchPose3, intakeMiddleLineUp))
                .setLinearHeadingInterpolation(launchPose3.getHeading(), intakeMiddleLineUp.getHeading())
                .addParametricCallback(0.1, ()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addPath(new BezierLine(intakeMiddleLineUp, intakeMiddleRow))
                .setLinearHeadingInterpolation(intakeMiddleLineUp.getHeading(), intakeMiddleRow.getHeading())
                .build();

        PathChain openGate = follower.pathBuilder()
                .addPath(new BezierLine(intakeMiddleRow, openGateForDCS))
                .setLinearHeadingInterpolation(intakeMiddleRow.getHeading(), openGateForDCS.getHeading())
                .build();

        PathChain openGateToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(openGateForDCS, launchPose1))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.5, HeadingInterpolator.linear(openGateForDCS.getHeading(), Math.toRadians(0))),
                        new HeadingInterpolator.PiecewiseNode(0.5, 1, HeadingInterpolator.facingPoint(facingGoalPoint))))
                .addParametricCallback(0.9, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.9, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.9, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.75,()->robotBase.intakeSubsystem.intake(1))
                .build();

        intakeMiddleRowPathLineUp = follower.pathBuilder()
                .addPath(new BezierLine(launchPose2, intakeMiddleLineUp))
                .setLinearHeadingInterpolation(launchPose2.getHeading(), intakeMiddleLineUp.getHeading())
                .build();

        intakeMiddleRowPath = follower.pathBuilder()
                .addPath(new BezierLine(intakeMiddleLineUp, intakeMiddleRow))
                .setConstantHeadingInterpolation(intakeMiddleRow.getHeading())
                .build();

        middleRowToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(openGateForDCS, launchPose3))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.5, HeadingInterpolator.linear(openGateForDCS.getHeading(), launchPose3.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.5, 1, HeadingInterpolator.constant(launchPose3.getHeading()))))
                //.addParametricCallback(0, ()->new SetAllVelocityCommandGroup(robotBase, 1900))
                /*.addParametricCallback(0.85, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.85, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.85, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))*/
                .addParametricCallback(0.9,()->robotBase.intakeSubsystem.intake(1))
                .build();
        launchMiddleToIntake = follower.pathBuilder()
                .addPath(new BezierLine(launchPose3, intakeArtifactsFromGate))
                .setConstantHeadingInterpolation(intakeArtifactsFromGate.getHeading())
                .addParametricCallback(0.1, ()-> CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addParametricCallback(0.5,()->robotBase.intakeSubsystem.intake(-1))
                /*.addPath(new BezierLine(intakeArtifactsFromGate, moveBackFromGateToIntake))
                .setLinearHeadingInterpolation(intakeArtifactsFromGate.getHeading(), moveBackFromGateToIntake.getHeading())*/
                .build();
        intakeFromGateToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(intakeArtifactsFromGate, launchPose3))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.5, HeadingInterpolator.linear(intakeArtifactsFromGate.getHeading(), launchPose3.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.5, 1, HeadingInterpolator.constant(launchPose3.getHeading()))))
                //.setLinearHeadingInterpolation(intakeArtifactsFromGate.getHeading(),launchAftIntakeFromGate.getHeading())
                .addParametricCallback(0.1, ()-> CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                /*.addParametricCallback(0.95, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.95, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.95, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))*/
                .addParametricCallback(0.5, ()->robotBase.intakeSubsystem.intake(1))
                .build();

        intakeTopRowPath = follower.pathBuilder()
                .addPath(new BezierLine(launchPose1, topRowLineUp))
                .setConstantHeadingInterpolation(topRowLineUp.getHeading())
                .addParametricCallback(0.1, ()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)))
                .addPath(new BezierLine(topRowLineUp, intakeTopRow))
                .setConstantHeadingInterpolation(topRowLineUp.getHeading())
                .addParametricCallback(0,()->robotBase.intakeSubsystem.intake(-1))
                .build();

        launchTopRow = follower.pathBuilder()
                .addPath(new BezierLine(intakeTopRow, launchPose2))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.25, HeadingInterpolator.linear(topRowLineUp.getHeading(), launchPose2.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.25, 1, HeadingInterpolator.constant(launchPose2.getHeading()))))
                .addParametricCallback(0.95, ()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.95, ()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.95, ()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH))
                .addParametricCallback(0.95,()->robotBase.intakeSubsystem.intake(1))
                .build();
        parking = follower.pathBuilder()
                .addPath(new BezierLine(launchPose2,park))
                .setConstantHeadingInterpolation(park.getHeading())
                .build();

        route = new SequentialCommandGroup(
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new InstantCommand(()->dblLaunchVel = 1800),
                new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE)),
                new FollowPathCommand(follower, startPath, true, 1),
                new WaitCommand(500),
                new TransferResetCommandGroup(robotBase),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new InstantCommand(()->dblLaunchVel = 1750),
                new FollowPathCommand(follower, intakeTopRowPath, true, 1),
                new FollowPathCommand(follower, launchTopRow,true,1),
                new WaitCommand(500),
                new TransferResetCommandGroup(robotBase),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPathCommand(follower, intakeMiddleRowPathLineUp, true,1),
                new FollowPathCommand(follower, intakeMiddleRowPath, true,1),
                new FollowPathCommand(follower, openGate, true,1),
                new FollowPathCommand(follower, middleRowToLaunch, true,1),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                new WaitCommand(500),
                new TransferResetCommandGroup(robotBase),
                new FollowPathCommand(follower, launchMiddleToIntake,true,1),
                new WaitCommand(1740),
                new FollowPathCommand(follower, intakeFromGateToLaunch, true, 1),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                new WaitCommand(500),
                new TransferResetCommandGroup(robotBase),
                new FollowPathCommand(follower, launchMiddleToIntake,true,1),
                new WaitCommand(1740),
                new FollowPathCommand(follower, intakeFromGateToLaunch, true, 1),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                new WaitCommand(500),
                new FollowPathCommand(follower, parking, true, 1)
                    /*
                    new WaitCommand(500),
                    new FollowPathCommand(follower, openGate, true, 0.25),
                    new FollowPathCommand(follower, openGateToLaunch, true, 1),
                    new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                    new FollowPathCommand(follower, intakeTopRowPath, true, 1),
                    new FollowPathCommand(follower, launchTopRow, false, 1),
                    //new WaitUntilCommand(()->!follower.isBusy()),
                    //new WaitCommand(250),
                    //new FollowPath(follower, launchPath, true, 1),
                    //new WaitUntilCommand(()->!follower.isBusy()),
                    //new FollowPathCommand(follower, intakeMiddleRowPathLineUp, true, 1),
                    //new InstantCommand(()->dblLaunchVel = 1750),
                    //new FollowPathCommand(follower, intakeMiddleRowPath, true, 1),
                    //new FollowPathCommand(follower, middleRowToLaunch, true, 1),
                    new WaitCommand(250),
                    new TransferResetCommandGroup(robotBase),
                    new FollowPathCommand(follower, launchMiddleToIntake,false,0.75),
                    new WaitCommand(1740),
                    new FollowPathCommand(follower, intakeFromGateToLaunch, true, 1),
                    new WaitCommand(250),
                    new FollowPathCommand(follower, launchMiddleToIntake, false, 0.75),
                    new WaitCommand(1750),
                    new FollowPathCommand(follower, intakeFromGateToLaunch, true, 1),
                    new WaitCommand(250),
                    new FollowPathCommand(follower, launchMiddleToIntake, false, 0.75),
                    new WaitCommand(1750),
                    new FollowPathCommand(follower, intakeFromGateToLaunch, true, 1),
                    new WaitCommand(250),
                    new FollowPathCommand(follower, intakeTopRowPath, true, 1),
                    new FollowPathCommand(follower, launchTopRow, true, 1),
                    new WaitCommand(250),
                    new FollowPathCommand(follower,parking,false,1),
                    new TransferResetCommandGroup(robotBase),
                    new InstantCommand(()->robotBase.launcherSubsystemLeft.setPower(0)),
                    new InstantCommand(()->robotBase.launcherSubsystemMiddle.setPower(0)),
                    new InstantCommand(()->robotBase.launcherSubsystemRight.setPower(0))*/
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
        Pose endPose = new Pose(follower.getPose().getX() - 5, follower.getPose().getY() + 5.5, follower.getPose().getHeading());
        DataStorage.endPosition = endPose;
        DataStorage.alliance = DecodeEnums.Alliance.BLUE;
    }
}
