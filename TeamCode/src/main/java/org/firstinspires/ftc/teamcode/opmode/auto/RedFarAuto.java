package org.firstinspires.ftc.teamcode.opmode.auto;

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
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.commands.DynamicVelocityCommand;
import org.firstinspires.ftc.teamcode.commands.TurretHeadingControlCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;


@Autonomous(name = "Red Far Auto")
public class RedFarAuto extends OpMode {

    Follower follower;
    RobotBase robotBase;
    SequentialCommandGroup path;
    int artifactsInBotCount;

    Pose startPose = new Pose(90, 10, Math.toRadians(90));
    Pose LaunchPose = new Pose(84, 18, Math.toRadians(0));
    Pose goalPose = new Pose(144, 138);

    BezierLine startToLaunch = new BezierLine(startPose, new Pose(84, 18, Math.toRadians(0)));

    BezierLine preIntakeLastRow = new BezierLine(
            new Pose(84, 18,Math.toRadians(0)),
            new Pose(101, 36));

    BezierLine intakeLastRow = new BezierLine(
            new Pose(101,36),
            new Pose(127,36));

    BezierLine launchLastRow =  new BezierLine(
            new Pose(127, 36),
            new Pose(84, 18,Math.toRadians(0)));

    BezierLine launchToHPIntake = new BezierLine(
            new Pose(84, 18,Math.toRadians(0)),
            new Pose(132, 9));

    BezierLine intakeHPToLaunch = new BezierLine(
            new Pose(132, 9),
            new Pose(84,18));

    BezierLine launchToPark = new BezierLine(
            new Pose(84, 18,Math.toRadians(0)),
            new Pose(110,11));



    PathChain startLaunch;
    PathChain intakeLastRowPath;
    PathChain launchLastRowPath;
    PathChain launchToHPZone;
    PathChain HPIntakeToLaunch;
    PathChain launchToParking;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);
        robotBase = new RobotBase(hardwareMap);

        startLaunch = follower.pathBuilder()
                .addPath(startToLaunch)
                .setLinearHeadingInterpolation(startPose.getHeading(), Math.toRadians(0))
                .build();

        intakeLastRowPath = follower.pathBuilder()
                .addPath(preIntakeLastRow)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(intakeLastRow)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
        launchLastRowPath = follower.pathBuilder()
                .addPath(launchLastRow)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        launchToHPZone = follower.pathBuilder()
                .addPath(launchToHPIntake)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
        HPIntakeToLaunch = follower.pathBuilder()
                .addPath(intakeHPToLaunch)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
        launchToParking = follower.pathBuilder()
                .addPath(launchToPark)
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();


        path = new SequentialCommandGroup(
                new InstantCommand(()->robotBase.hoodSubsystem.far()),
                //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1900)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.4)),
                new FollowPathCommand(follower, startLaunch, true, 1),
                new WaitCommand(650),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(750),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new FollowPathCommand(follower, intakeLastRowPath, false, 0.6),
                new FollowPathCommand(follower, launchLastRowPath,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(750),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
               /* new FollowPathCommand(follower, launchToHPZone, true, 1),
                new WaitCommand(200),
                new FollowPathCommand(follower, HPIntakeToLaunch, true, 1),
              //  new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
               // new WaitCommand(100),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(500),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new FollowPathCommand(follower, launchToHPZone, true, 1),
                new WaitCommand(200),
                new FollowPathCommand(follower, HPIntakeToLaunch, true, 1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
               // new WaitCommand(100),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(500),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),*/
                new FollowPathCommand(follower, launchToParking, true, 1),


                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE))
        );

        new Trigger(()->robotBase.intakeLIntakeDistanceSensorSubsystem.getDistance() <= 6 &&
                robotBase.intakeRIntakeDistanceSensorSubsystem.getDistance() <= 7)
                .whenActive(()->artifactsInBotCount++);

        new Trigger(()->artifactsInBotCount > 2)
                .whenActive(new SequentialCommandGroup(
                        new WaitCommand(100),
                        new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK))
                ));
        //robotBase.turretSubsystem.updatePosition(180);
        robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE);
        robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK);
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
        CommandScheduler.getInstance().schedule(path);
        CommandScheduler.getInstance().schedule(new TurretHeadingControlCommandGroup(robotBase, follower));
        //CommandScheduler.getInstance().schedule(new DynamicVelocityCommand(robotBase, follower));
    }

    @Override
    public void loop() {
        follower.update();
        robotBase.launcherSubsystem.setVelocity(2100);
        //robotBase.hoodSubsystem.setDynamicPosition(follower.getPose().distanceFrom(goalPose));

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
