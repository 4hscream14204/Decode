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
import org.firstinspires.ftc.teamcode.commands.AutoTurretHeadingCommand;
import org.firstinspires.ftc.teamcode.commands.DynamicVelocityCommand;
import org.firstinspires.ftc.teamcode.commands.TurretHeadingControlCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;


@Autonomous(name = "Blue Gate Auto")
public class BlueGateAuto extends OpMode {

    Follower follower;
    RobotBase robotBase;
    SequentialCommandGroup path;
    int artifactsInBotCount;

    Pose startPose = new Pose(125, 131, Math.toRadians(-44)).mirror();
   Pose goalPose = new Pose(144, 138);
//144 138
    BezierLine startToLaunch = new BezierLine(startPose, new Pose(93.000, 96.000, Math.toRadians(0)).mirror());

    BezierCurve preIntakeSecondRow = new BezierCurve(
            new Pose(93.000, 96.000).mirror(),
            new Pose(89.054, 60.888).mirror(),
            new Pose(108.000, 60.000).mirror());

    BezierLine intakeSecondRow =  new BezierLine(
            new Pose(108.000, 60.000).mirror(),
            new Pose(135.000, 60.000).mirror());

    BezierLine secondRowToLaunch = new BezierLine(
            new Pose(135, 60).mirror(),
            new Pose(90, 83).mirror());

    BezierLine launchToGateLineUp = new BezierLine(
            new Pose(85, 85).mirror(),
            new Pose(136,63,Math.toRadians(136)).mirror());

    BezierLine gateToLaunchLine = new BezierLine(
            new Pose(136,63,Math.toRadians(136)).mirror(),
            new Pose(85, 85).mirror());

    BezierLine preIntakeFirstRow = new BezierLine(
            new Pose(85,85).mirror(),
            new Pose(105,85).mirror());

    BezierLine intakeFirstRow = new BezierLine(
            new Pose(105,85).mirror(),
            new Pose(127,85).mirror());

    BezierLine firstRowToLaunch = new BezierLine(
            new Pose(127,85).mirror(),
            new Pose(88,113).mirror());


    PathChain startLaunch;
    PathChain intakeSecondRowPath;
    PathChain launchSecondRowPath;
    PathChain launchToLineUpToGate;
    PathChain intakeFromGate;
    PathChain gateToLaunch;
    PathChain preIntakeFirstRowPath;
    PathChain launchFirstRowPath;


    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);
        robotBase = new RobotBase(hardwareMap);

        startLaunch = follower.pathBuilder()
                .addPath(startToLaunch)
                .setLinearHeadingInterpolation(startPose.getHeading(), Math.toRadians(180))
                //.addParametricCallback(0.97, ()->CommandScheduler.getInstance().schedule(new WaitCommand(1000)))
                .build();

        intakeSecondRowPath = follower.pathBuilder()
                .addPath(preIntakeSecondRow)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(intakeSecondRow)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        launchSecondRowPath = follower.pathBuilder()
                .addPath(secondRowToLaunch)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        launchToLineUpToGate = follower.pathBuilder()
                .addPath(launchToGateLineUp)
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(136))
                .build();
       /* intakeFromGate = follower.pathBuilder()
                .addPath(intakeFromRamp)
                .setLinearHeadingInterpolation(Math.toRadians(0),Math.toRadians(39))
                .build();
*/
        gateToLaunch = follower.pathBuilder()
                .addPath(gateToLaunchLine)
                .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))
                .build();

        preIntakeFirstRowPath = follower.pathBuilder()
                .addPath(preIntakeFirstRow)
                .setLinearHeadingInterpolation(Math.toRadians(180),Math.toRadians(180), 0.8)
                .addPath(intakeFirstRow)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        launchFirstRowPath = follower.pathBuilder()
                .addPath(firstRowToLaunch)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        path = new SequentialCommandGroup(
                //new InstantCommand(()->robotBase.hoodSubsystem.close()),
               // new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1600)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
                new FollowPathCommand(follower, startLaunch, true, 1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(200),
                new InstantCommand(()->artifactsInBotCount = 0),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new FollowPathCommand(follower, intakeSecondRowPath, false, 0.6),
                new FollowPathCommand(follower, launchSecondRowPath,true,1),
                //new WaitCommand(100),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(200),
                new InstantCommand(()->artifactsInBotCount = 0),
                 new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                //new TransferCommand(robotBase, follower),
                //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1000)),
                //new WaitCommand(1000),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new FollowPathCommand(follower, launchToLineUpToGate, true, 1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount >= 3).withTimeout(900),
                //     new FollowPathCommand(follower,intakeFromGate,true,1),
                // new WaitCommand(1100),
                // new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new FollowPathCommand(follower, gateToLaunch, true, 1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
               // new WaitCommand(100),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(200),
                new InstantCommand(()->artifactsInBotCount = 0),
                 new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                //new TransferCommand(robotBase, follower),
                //new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(1000)),
                //new WaitCommand(500),
                new FollowPathCommand(follower, launchToLineUpToGate, false, 1),
                //new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount >= 3).withTimeout(900),
                //       new FollowPathCommand(follower,intakeFromGate,false,1),
                //  new WaitCommand(1100),
                //   new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                new FollowPathCommand(follower, gateToLaunch, true, 1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                //new WaitCommand(100),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(200),
                new InstantCommand(()->artifactsInBotCount = 0),
                 new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new FollowPathCommand(follower, launchToLineUpToGate,true,1),
                //new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount >= 3).withTimeout(900),
                new FollowPathCommand(follower,gateToLaunch,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
                //new WaitCommand(100),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(200),
                new InstantCommand(()->artifactsInBotCount = 0),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new FollowPathCommand(follower, launchToLineUpToGate,true,1),
                //new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount >= 3).withTimeout(900),
                new FollowPathCommand(follower, gateToLaunch,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
               // new WaitCommand(100),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(200),
                new InstantCommand(()->artifactsInBotCount = 0),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),

           //

           /*
           REMOVED THIS CYCLE FOR TIME PURPOSES
           // new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
            new FollowPathCommand(follower, launchToLineUpToGate,true,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitUntilCommand(()->artifactsInBotCount >= 3).withTimeout(800),
               // new FollowPathCommand(follower,intakeFromGate,true,1),
          //  new WaitCommand(1100),
              //  new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
            new FollowPathCommand(follower,gateToLaunch,true,1),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK)),
            new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
            new WaitCommand(150),
                new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE)),
                new InstantCommand(()->artifactsInBotCount = 0),
            */
              new FollowPathCommand(follower,preIntakeFirstRowPath,true,0.6),
                new FollowPathCommand(follower,launchFirstRowPath,false,1),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new WaitCommand(100),
                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new WaitCommand(250),
                //new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                //
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
        DataStorage.alliance = DecodeEnums.Alliance.BLUE;
    }

    @Override
    public void start() {
        follower.setStartingPose(new Pose(startPose.getX(), startPose.getY(), Math.toRadians(-44)));
        CommandScheduler.getInstance().schedule(path);
        CommandScheduler.getInstance().schedule(new AutoTurretHeadingCommand(robotBase, follower, new Pose(-24, 144)));
        CommandScheduler.getInstance().schedule(new DynamicVelocityCommand(robotBase, follower));
    }

    @Override
    public void loop() {
        follower.update();
        robotBase.hoodSubsystem.setDynamicPosition(follower.getPose().distanceFrom(goalPose));

        telemetry.addData("X: ", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Turret Heading Target", robotBase.turretSubsystem.degreeModulus);
        CommandScheduler.getInstance().run();
    }
    @Override
    public void stop(){
        Pose endPose = new Pose(follower.getPose().getX(), follower.getPose().getY(), follower.getPose().getHeading());
        DataStorage.endPosition = endPose;
        DataStorage.alliance = DecodeEnums.Alliance.BLUE;
    }
}

