package org.firstinspires.ftc.teamcode.pedropathing.routes;


import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commandgroups.auto.AutoTransferAndLaunchCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.CameraLight;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

@Autonomous(name = "12 Artifact Big Launch Zone Red Auto")
public class LargeLaunchZone12Artifacts extends OpMode {
    RobotBase robotBase;
    Follower follower;
    SequentialCommandGroup route;
    AutoTransferAndLaunchCommandGroup autoTransferAndLaunchCommandGroup;
    Pose startPose = new Pose(111.62, 135.55, Math.toRadians(180));
    Pose parkPose = new Pose(127, 81, Math.toRadians(270));
    Pose launchPose = new Pose(88, 98, Math.toRadians(45));
    Pose startToLaunchControl = new Pose(89.321, 136.355, Math.toRadians(0));
    Pose launchToTopRowControl = new Pose(79, 84, Math.toRadians(0));
    Pose preIntakeTopRow = new Pose(94, 84, Math.toRadians(0));
    Pose intakeTopRow = new Pose(125, 84, Math.toRadians(0));
    Pose moveBackFromFirstRow = new Pose(92,84);
    Pose lineUpToOpenRamp = new Pose(125, 72, Math.toRadians(0));
    Pose openRamp = new Pose(131, 72, Math.toRadians(0));
    Pose topRowToLaunchControl = new Pose(90.9, 78.23, Math.toRadians(0));
    Pose launchToMiddleRow = new Pose(74.000, 62.000, Math.toRadians(0));
    Pose preIntakeMiddleRow = new Pose(94, 60, Math.toRadians(0));
    Pose intakeMiddleRow = new Pose(140, 60, Math.toRadians(0));
    Pose middleRowToLaunchControl = new Pose(79.604, 54.688, Math.toRadians(0));
    Pose launchToBottomRowControl = new Pose(77.016, 85.753, Math.toRadians(0));
    Pose preIntakeBottomRow = new Pose(104.178, 35, Math.toRadians(0));
    Pose intakeBottomRow = new Pose(140, 35, Math.toRadians(0));
    Pose backsUpFromBottomRow = new Pose(104,35,Math.toRadians(0));
    Pose bottomRowToLaunchControl = new Pose(99.020, 40.449);
    PathChain goesFromWallToShootPreload;
    PathChain linesUpToIntakeThirdRow;
    PathChain intakesThirdRow;
    PathChain backsUpFromThirdRow;
    PathChain goesToShootThirdRow;
    PathChain linesUpWithSecondRow;
    PathChain intakesSecondRow;
    PathChain backUpFromSecondRow;
    PathChain goesToShootSecondRow;
    PathChain linesUpToIntakeFirstRow;
    PathChain intakesFirstRow;
    PathChain shootsFirstRow;
    PathChain movesBackFromIntaking;
    PathChain linesUpToOpenRamp;
    PathChain opensRamp;
    PathChain park;
    GamepadEx chassis;
    DesiredRows desiredRows = DesiredRows.THREE;
    SequentialCommandGroup routeMiddleRow;
    SequentialCommandGroup routeBottomRow;
    boolean middleRowDone = false;
    boolean bottomRowDone = false;
    int secondsToWait = 0;
    ElapsedTime timer;

    public enum DesiredRows{
        ONE,
        TWO,
        THREE
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        CommandScheduler.getInstance().reset();
        chassis = new GamepadEx(gamepad1);
        timer = new ElapsedTime();
        robotBase = new RobotBase(hardwareMap);
        CommandScheduler.getInstance().schedule(new InstantCommand(()-> robotBase.sorterCameraSubsystem.getAnalysis()));
        //autoTransferAndLaunchCommandGroup = new AutoTransferAndLaunchCommandGroup(robotBase, 1750);

        chassis.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->desiredRows = DesiredRows.THREE)));

        chassis.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->desiredRows = DesiredRows.TWO)));

        chassis.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->desiredRows = DesiredRows.ONE)));

        chassis.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->secondsToWait = secondsToWait + 1000)));

        chassis.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->secondsToWait = secondsToWait - 1000)));

        goesFromWallToShootPreload = follower.pathBuilder()
                .addPath(new BezierLine(startPose, launchPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), launchPose.getHeading())
                .build();

        //First line//
        linesUpToIntakeFirstRow = follower.pathBuilder()
                .addPath(new BezierLine(launchPose, preIntakeTopRow))
                .setLinearHeadingInterpolation(launchPose.getHeading(), preIntakeTopRow.getHeading())

                .build();
        intakesFirstRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(preIntakeTopRow, intakeTopRow))
                .setConstantHeadingInterpolation(preIntakeTopRow.getHeading())
                .build();
        shootsFirstRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(intakeTopRow, launchPose))
                .setLinearHeadingInterpolation(intakeTopRow.getHeading(), launchPose.getHeading()-Math.toRadians(5))
                .build();
        //Opens Classifier Ramp//

        linesUpToOpenRamp = follower.pathBuilder()
                .addPath(
                        new BezierLine(intakeTopRow,lineUpToOpenRamp))
                .setConstantHeadingInterpolation(intakeTopRow.getHeading())
                .build();
        opensRamp = follower.pathBuilder()
                .addPath(
                        new BezierLine(lineUpToOpenRamp,openRamp))
                .setConstantHeadingInterpolation(lineUpToOpenRamp.getHeading())
                .build();


        //Second line//
        linesUpWithSecondRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(launchPose, preIntakeMiddleRow))
                .setLinearHeadingInterpolation(launchPose.getHeading(), preIntakeMiddleRow.getHeading())
                .build();
        intakesSecondRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(preIntakeMiddleRow, intakeMiddleRow))
                .setConstantHeadingInterpolation(preIntakeMiddleRow.getHeading())
                .build();
        backUpFromSecondRow = follower.pathBuilder()
                .addPath(
                        new BezierLine( intakeMiddleRow,preIntakeMiddleRow))
                .setConstantHeadingInterpolation(preIntakeMiddleRow.getHeading())
                .build();

        goesToShootSecondRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(preIntakeMiddleRow, launchPose))
                .setLinearHeadingInterpolation(preIntakeMiddleRow.getHeading(), launchPose.getHeading()-Math.toRadians(5))
                .build();

        //Third line//
        linesUpToIntakeThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierCurve(launchPose, launchToBottomRowControl, preIntakeBottomRow))
                .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(0))
                .build();
        intakesThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(preIntakeBottomRow, intakeBottomRow))
                .setConstantHeadingInterpolation(0)
                .build();
        backsUpFromThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(intakeBottomRow, backsUpFromBottomRow))
                .setConstantHeadingInterpolation(0)
                .build();
        goesToShootThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierCurve(intakeBottomRow, bottomRowToLaunchControl, launchPose))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(135))
                .build();
        //park//

        park = follower.pathBuilder()
                .addPath(new BezierLine(launchPose, parkPose))
                .setLinearHeadingInterpolation(Math.toRadians(43), Math.toRadians(270))
                .build();

        follower = Constants.createFollower(hardwareMap);

        routeMiddleRow = new SequentialCommandGroup(
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, linesUpWithSecondRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, intakesSecondRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(0)),
                new FollowPath(follower, goesToShootSecondRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new LaunchCommandGroup(robotBase),
                //new InstantCommand(()->patternCommandGroup.schedule()),
                //new WaitUntilCommand(()->patternCommandGroup.isFinished()),
                new InstantCommand(()->middleRowDone = true));

        routeBottomRow = new SequentialCommandGroup(
                new FollowPath(follower, linesUpToIntakeThirdRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, intakesThirdRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(0)),
                new FollowPath(follower, goesToShootThirdRow, true, 1),
                //new LaunchCommandGroup(robotBase),
                //new InstantCommand(()->patternCommandGroup.schedule()),
                //new WaitUntilCommand(()->patternCommandGroup.isFinished()),
                new InstantCommand(()->bottomRowDone = true)
        );

        route = new SequentialCommandGroup(
                new WaitUntilCommand(()->(secondsToWait) <= timer.milliseconds()),
               // new AutoTransferAndLaunchCommandGroup(robotBase, 1750),
                new FollowPath(follower, goesFromWallToShootPreload, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new AutoTransferAndLaunchCommandGroup(robotBase, 1750),
                //new WaitCommand(3000),
                new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0)),
                new FollowPath(follower, linesUpToIntakeFirstRow, false, 1),
                //new WaitUntilCommand(()->autoTransferAndLaunchCommandGroup.isFinished()),
                new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, intakesFirstRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower,linesUpToOpenRamp,true,1 ),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower,opensRamp,true,1),
                new WaitUntilCommand(()->!follower.isBusy()),
                // new WaitCommand(1000),
                // new InstantCommand(()->robotBase.intakeSubsystem.intake(0)),
                new AutoTransferAndLaunchCommandGroup(robotBase, 1750),
                new FollowPath(follower, shootsFirstRow, false, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                //new AutoTransferAndLaunchCommandGroup(robotBase, 1750),
                new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0)),
                new FollowPath(follower, linesUpWithSecondRow),
                new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, intakesSecondRow),
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, backUpFromSecondRow),
                new WaitUntilCommand(()->!follower.isBusy()),
                new AutoTransferAndLaunchCommandGroup(robotBase, 1750),
                new FollowPath(follower, goesToShootSecondRow),
                new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0)),
                new FollowPath(follower, linesUpToIntakeThirdRow),
                new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower,intakesThirdRow),
                new WaitUntilCommand(()->!follower.isBusy()),
               new FollowPath(follower, backsUpFromThirdRow),
                new WaitUntilCommand(()->!follower.isBusy()),
                new AutoTransferAndLaunchCommandGroup(robotBase, 1750),
               new FollowPath(follower,goesToShootThirdRow),
                new WaitUntilCommand(()->!follower.isBusy()),
               // new AutoTransferAndLaunchCommandGroup(robotBase, 1750),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(0)),
                new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0)),
                new FollowPath(follower, park, true, 1));


        /*
                //new LaunchCommandGroup(robotBase),
                //new InstantCommand(()->patternCommandGroup.schedule()),
                //new WaitUntilCommand(()->patternCommandGroup.isFinished()),
                new ConditionalCommand(new InstantCommand(()->routeMiddleRow.schedule()), new InstantCommand(()->follower.followPath(park)),()-> (desiredRows == DesiredRows.TWO || desiredRows == DesiredRows.THREE)),
                new WaitUntilCommand(()-> middleRowDone),
                new ConditionalCommand(new InstantCommand(()->routeBottomRow.schedule()), new InstantCommand(()->follower.followPath(park)), ()-> (desiredRows == DesiredRows.THREE)),
                new WaitUntilCommand(()->bottomRowDone),
                new FollowPath(follower, park, true, 1));*/
/*
        /*CommandScheduler.getInstance().schedule(new InstantCommand(()-> robotBase.sorterCameraSubsystem.getAnalysis()));
        CommandScheduler.getInstance().schedule(new InstantCommand(()-> robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.OBELISK)));*/
    }

    @Override
    public void init_loop(){
        CommandScheduler.getInstance().run();
        robotBase.sorterCameraSubsystem.getAnalysis();
        chassis.readButtons();
        telemetry.addData("Rows", desiredRows);
        telemetry.addData("Wait Time", secondsToWait);
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading: ", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Is Running", robotBase.limelightSubsystem.limelight.isRunning());
        telemetry.addData("Is connected: ", robotBase.limelightSubsystem.limelight.isConnected());
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
        CommandScheduler.getInstance().schedule(route);
        robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.OBELISK);
        robotBase.cameraLightSubsystemRight.setShade(CameraLight.Shades.FULL);
        robotBase.cameraLightSubsystemLeft.setShade(CameraLight.Shades.FULL);
        timer.reset();
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        robotBase.limelightSubsystem.updateLimelight();
        robotBase.sorterCameraSubsystem.getAnalysis();
        follower.update();
        if(robotBase.limelightSubsystem.id == 23){
            DataStorage.pattern = DecodeEnums.Patterns.PPG;
        }
        else if(robotBase.limelightSubsystem.id == 22){
            DataStorage.pattern = DecodeEnums.Patterns.PGP;
        }
        else if (robotBase.limelightSubsystem.id == 21){
            DataStorage.pattern = DecodeEnums.Patterns.GPP;
        }
        else{
            DataStorage.pattern = DecodeEnums.Patterns.PPG;
        }
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading: ", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Is busy", follower.isBusy());
        telemetry.addData("Rows", desiredRows);
        telemetry.addData("MS", secondsToWait);
        telemetry.addData("ID: ", robotBase.limelightSubsystem.id);
        telemetry.addData("Pattern ", DataStorage.pattern);
        telemetry.addData("Left Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchLeft());
        telemetry.addData("Middle Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchMiddle());
        telemetry.addData("Right Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchRight());
    }
    @Override
    public void stop(){
        robotBase.limelightSubsystem.limelight.stop();
        DataStorage.endPosition = follower.getPose();
        DataStorage.alliance = DecodeEnums.Alliance.RED;
    }
}
