package org.firstinspires.ftc.teamcode.pedropathing.routes;


import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.commandgroups.auto.AutoTransferAndLaunchCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.auto.AutoTransferAndLaunchNoPatternCG;
import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.CameraLight;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

@Autonomous(name = "Small Red Auto Route Human")

public class SmallRedAutoRouteHP extends OpMode {
    RobotBase robotBase;
    Follower follower;
    SequentialCommandGroup route;
    AutoTransferAndLaunchCommandGroup autoTransferAndLaunchCommandGroup;

    Pose startPose = new Pose(93,7,Math.toRadians(0));
    Pose launchPose = new Pose(87,14,Math.toRadians(66));
    Pose intakePose = new Pose(138.5,7.5,Math.toRadians(0));


    PathChain goesToShootPreload;
    PathChain intakesFromHumanZone;
    PathChain goesBackToShoot;
    GamepadEx chassis;


    ElapsedTime timer;
    Servo prism;
    int secondsToWait = 0;
    double dblLaucnhVel = 2200;
    double dblPreLaucnhVel = 2200;


    @Override
    public void init() {
        DataStorage.alliance = DecodeEnums.Alliance.RED;
        follower = Constants.createFollower(hardwareMap);
        prism = hardwareMap.get(Servo.class, "prism");
        CommandScheduler.getInstance().reset();
        CommandScheduler.getInstance().schedule(new InstantCommand(() -> prism.setPosition(0.225)));
        chassis = new GamepadEx(gamepad1);
        timer = new ElapsedTime();
        robotBase = new RobotBase(hardwareMap);
        CommandScheduler.getInstance().schedule(new InstantCommand(() -> robotBase.sorterCameraSubsystem.getAnalysis()));


        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);

        goesToShootPreload = follower.pathBuilder()
                .addPath(new BezierLine(startPose, launchPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), launchPose.getHeading())
                .build();

        intakesFromHumanZone = follower.pathBuilder()
                .addPath(new BezierLine(launchPose, intakePose))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0,0.5,HeadingInterpolator.linear(launchPose.getHeading() , intakePose.getHeading())),
                        new HeadingInterpolator.PiecewiseNode(0.5,1, HeadingInterpolator.constant(intakePose.getHeading()))))
                                .build();


        goesBackToShoot = follower.pathBuilder()
                .addPath(new BezierLine(intakePose, launchPose))
                .setLinearHeadingInterpolation(intakePose.getHeading(), launchPose.getHeading())
                .build();


        route = new SequentialCommandGroup(
                new WaitUntilCommand(() -> (secondsToWait) <= timer.milliseconds()),
                new SetAllVelocityCommandGroup(robotBase, dblLaucnhVel),
                new InstantCommand(() -> robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.FAR)),
                new FollowPathCommand(follower, goesToShootPreload, true, 1),
                new AutoTransferAndLaunchNoPatternCG(robotBase,dblLaucnhVel),
                new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPathCommand(follower,intakesFromHumanZone,true,1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPathCommand(follower,goesBackToShoot,false,1),
                new AutoTransferAndLaunchNoPatternCG(robotBase,dblLaucnhVel),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(1))
                );

    }
        @Override
        public void start () {
            follower.setStartingPose(startPose);
            CommandScheduler.getInstance().schedule(route);
            robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.OBELISK);
            //  robotBase.cameraLightSubsystemRight.setShade(CameraLight.Shades.HALF);
            robotBase.cameraLightSubsystemLeft.setShade(CameraLight.Shades.HALF);
            // new InitSorterLightsCommandGroup(robotBase);
            timer.reset();
        }

     @Override
     public void loop(){
            CommandScheduler.getInstance().run();
         CommandScheduler.getInstance().schedule(
                 new SetAllVelocityCommandGroup(robotBase, dblPreLaucnhVel)
         );
            robotBase.limelightSubsystem.updateLimelight();
            robotBase.sorterCameraSubsystem.getAnalysis();
            follower.update();
            if (robotBase.limelightSubsystem.id == 23) {
                DataStorage.pattern = DecodeEnums.Patterns.PPG;
            } else if (robotBase.limelightSubsystem.id == 22) {
                DataStorage.pattern = DecodeEnums.Patterns.PGP;
            } else if (robotBase.limelightSubsystem.id == 21) {
                DataStorage.pattern = DecodeEnums.Patterns.GPP;
            } else {
                DataStorage.pattern = DecodeEnums.Patterns.PPG;
            }
            telemetry.addData("X", follower.getPose().getX());
            telemetry.addData("Y: ", follower.getPose().getY());
            telemetry.addData("Heading: ", Math.toDegrees(follower.getPose().getHeading()));
            telemetry.addData("Is busy", follower.isBusy());
            telemetry.addData("MS", secondsToWait);
            telemetry.addData("ID: ", robotBase.limelightSubsystem.id);
            telemetry.addData("Pattern ", DataStorage.pattern);
            telemetry.addData("Left Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchLeft());
            telemetry.addData("Middle Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchMiddle());
            telemetry.addData("Right Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchRight());
            telemetry.addData("Is Stuck", follower.isRobotStuck());
        }
        @Override
        public void stop () {
            robotBase.limelightSubsystem.limelight.stop();
            DataStorage.endPosition = follower.getPose();
            DataStorage.alliance = DecodeEnums.Alliance.RED;
        }
    }
