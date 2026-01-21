package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commandgroups.auto.AutoTransferAndLaunchCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.auto.AutoTransferAndLaunchNoPatternCG;
import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.CameraLight;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

@Autonomous(name = "Blue 12 Artifact Big Launch Zone Auto")
public class BlueLargeLaunchZone12Artifacts extends OpMode {
    RobotBase robotBase;
    Follower follower;
    SequentialCommandGroup route;
    AutoTransferAndLaunchCommandGroup autoTransferAndLaunchCommandGroup;
    Pose startPose = new Pose(111.62, 135.55, Math.toRadians(180)).mirror();
    Pose parkPose = new Pose(106, 74, Math.toRadians(270)).mirror();
    //Pose launchPose = new Pose(88, 98, Math.toRadians(45)).mirror();
    Pose launchPose = new Pose(86, 90, Math.toRadians(47)).mirror();
    Pose startToLaunchControl = new Pose(89.321, 136.355, Math.toRadians(0)).mirror();
    Pose launchToTopRowControl = new Pose(79, 84, Math.toRadians(0)).mirror();
    Pose preIntakeTopRow = new Pose(94, 84, Math.toRadians(0)).mirror();
    Pose intakeTopRow = new Pose(125, 84, Math.toRadians(0)).mirror();
    Pose moveBackFromFirstRow = new Pose(92,84, Math.toRadians(90)).mirror();
    Pose lineUpToOpenRamp = new Pose(125, 76, Math.toRadians(90)).mirror();
    Pose openRamp = new Pose(126, 76, Math.toRadians(90)).mirror();
    Pose topRowToLaunchControl = new Pose(90.9, 78.23, Math.toRadians(0)).mirror();
    Pose launchToMiddleRow = new Pose(74.000, 62.000, Math.toRadians(0)).mirror();
    Pose preIntakeMiddleRow = new Pose(94, 60, Math.toRadians(0)).mirror();
    Pose intakeMiddleRow = new Pose(132, 60, Math.toRadians(0)).mirror();
    Pose backupMiddleRow = new Pose(114, 60, Math.toRadians(0)).mirror();
    Pose middleRowToLaunchControl = new Pose(79.604, 54.688, Math.toRadians(0)).mirror();
    Pose launchToBottomRowControl = new Pose(77.016, 85.753, Math.toRadians(0)).mirror();
    Pose preIntakeBottomRow = new Pose(94, 39, Math.toRadians(0)).mirror();
    Pose intakeBottomRow = new Pose(132, 39, Math.toRadians(0)).mirror();
    Pose backsUpFromBottomRow = new Pose(104,35,Math.toRadians(0)).mirror();
    Pose bottomRowToLaunchControl = new Pose(99.020, 40.449).mirror();
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
    double dblLaucnhVel = 1845;
    double dblPreLaucnhVel = 1845;
    ElapsedTime timer;

    public enum DesiredRows{
        ONE,
        TWO,
        THREE
    }

    @Override
    public void init() {
        DataStorage.alliance = DecodeEnums.Alliance.BLUE;
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
                .setLinearHeadingInterpolation(startPose.getHeading(), launchPose.getHeading()-Math.toRadians(1))
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
                .setLinearHeadingInterpolation(intakeTopRow.getHeading(), launchPose.getHeading()+Math.toRadians(7))
                .build();
        //Opens Classifier Ramp//

        /*movesBackFromIntaking = follower.pathBuilder()
                .addPath(new BezierLine(intakeTopRow, moveBackFromFirstRow))
                .setConstantHeadingInterpolation(moveBackFromFirstRow.getHeading())
                .build();*/

        linesUpToOpenRamp = follower.pathBuilder()
                .addPath(
                        new BezierLine(intakeTopRow, lineUpToOpenRamp))
                .setConstantHeadingInterpolation(lineUpToOpenRamp.getHeading())
                .build();
        opensRamp = follower.pathBuilder()
                .addPath(
                        new BezierLine(lineUpToOpenRamp,openRamp))
                .setLinearHeadingInterpolation(lineUpToOpenRamp.getHeading(), openRamp.getHeading())
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
                        new BezierLine( intakeMiddleRow,backupMiddleRow))
                .setConstantHeadingInterpolation(backupMiddleRow.getHeading())
                .addPath(new BezierLine(backupMiddleRow, launchPose))
                .setLinearHeadingInterpolation(backupMiddleRow.getHeading(), launchPose.getHeading()+Math.toRadians(5))
                .build();

        goesToShootSecondRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(preIntakeMiddleRow, launchPose))
                .setLinearHeadingInterpolation(preIntakeMiddleRow.getHeading(), launchPose.getHeading()+Math.toRadians(7))
                .build();

        //Third line//
        linesUpToIntakeThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(launchPose, preIntakeBottomRow))
                .setLinearHeadingInterpolation(launchPose.getHeading(), preIntakeBottomRow.getHeading())
                .build();
        intakesThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(preIntakeBottomRow, intakeBottomRow))
                .setConstantHeadingInterpolation(preIntakeBottomRow.getHeading())
                .build();
        backsUpFromThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(intakeBottomRow, backsUpFromBottomRow))
                .setConstantHeadingInterpolation(intakeBottomRow.getHeading())
                .build();
        goesToShootThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(intakeBottomRow, launchPose))
                .setLinearHeadingInterpolation(intakeBottomRow.getHeading(), launchPose.getHeading()+Math.toRadians(6))
                .build();
        //park//

        park = follower.pathBuilder()
                .addPath(new BezierLine(launchPose, parkPose))
                .setLinearHeadingInterpolation(launchPose.getHeading(), parkPose.getHeading())
                .build();

        follower = Constants.createFollower(hardwareMap);


        routeMiddleRow = new SequentialCommandGroup(
                new FollowPath(follower, linesUpWithSecondRow),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, intakesSecondRow).withTimeout(1500),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new SetAllVelocityCommandGroup(robotBase, dblPreLaucnhVel),
                //Back up and go to launch pose
                new FollowPath(follower, backUpFromSecondRow, 1),
                //new WaitUntilCommand(()->!follower.isBusy()),
                //new FollowPath(follower, goesToShootSecondRow),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                new AutoTransferAndLaunchCommandGroup(robotBase, dblLaucnhVel),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                new ConditionalCommand(new InstantCommand(()->routeBottomRow.schedule()), new FollowPath(follower, park, true, 1), ()->desiredRows == DesiredRows.THREE)
        );

        routeBottomRow = new SequentialCommandGroup(
                new FollowPath(follower, linesUpToIntakeThirdRow),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                //new SetAllVelocityCommandGroup(robotBase,1800),
                new FollowPath(follower,intakesThirdRow).withTimeout(1500),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new SetAllVelocityCommandGroup(robotBase, dblPreLaucnhVel),
                new FollowPath(follower,goesToShootThirdRow),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                new AutoTransferAndLaunchCommandGroup(robotBase, dblLaucnhVel),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                // new FollowPath(follower,goesToShootThirdRow),
                // new WaitUntilCommand(()->!follower.isBusy()),
                new SetAllVelocityCommandGroup(robotBase, 0),
                new FollowPath(follower, park, true, 1),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(0)));

        route = new SequentialCommandGroup(
                new WaitUntilCommand(()->(secondsToWait) <= timer.milliseconds()),
                new SetAllVelocityCommandGroup(robotBase, dblPreLaucnhVel),
                new FollowPath(follower, goesFromWallToShootPreload, true, 1),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new AutoTransferAndLaunchNoPatternCG(robotBase, dblLaucnhVel),
                //new Transfer3BallsNoCameraCommandGroup(robotBase),
                //new WaitCommand(3000),
                //new SetAllVelocityCommandGroup(robotBase, 0),
                new FollowPath(follower, linesUpToIntakeFirstRow, false, 1),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, intakesFirstRow, true, 1).withTimeout(1200),
                //new WaitUntilCommand(()->!follower.isBusy()),
                /*new FollowPath(follower, movesBackFromIntaking, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),*/
                new FollowPath(follower,linesUpToOpenRamp,true,1 ),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower,opensRamp,true,1).withTimeout(500),
                new WaitCommand(500),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new SetAllVelocityCommandGroup(robotBase, dblPreLaucnhVel),
                // new WaitCommand(1000),
                // new InstantCommand(()->robotBase.intakeSubsystem.intake(0)),
                new FollowPath(follower, shootsFirstRow, false, 1),
                //new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                new AutoTransferAndLaunchCommandGroup(robotBase, dblLaucnhVel),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                new ConditionalCommand(new InstantCommand(()->routeMiddleRow.schedule()), new InstantCommand(()->new FollowPath(follower, park, true, 1)), ()->desiredRows == DesiredRows.TWO || desiredRows == DesiredRows.THREE));
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
        robotBase.cameraLightSubsystemRight.setShade(CameraLight.Shades.HALF);
        robotBase.cameraLightSubsystemLeft.setShade(CameraLight.Shades.HALF);
        // new InitSorterLightsCommandGroup(robotBase);
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
        telemetry.addData("Is Stuck", follower.isRobotStuck());
    }
    @Override
    public void stop(){
        robotBase.limelightSubsystem.limelight.stop();
        DataStorage.endPosition = follower.getPose();
        DataStorage.alliance = DecodeEnums.Alliance.BLUE;
    }
}


