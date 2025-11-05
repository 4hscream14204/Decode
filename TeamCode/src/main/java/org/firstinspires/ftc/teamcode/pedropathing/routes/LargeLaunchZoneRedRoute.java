package org.firstinspires.ftc.teamcode.pedropathing.routes;


import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
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

import org.firstinspires.ftc.teamcode.commandgroups.AutoTransferAndLaunchCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.LaunchCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.TransferPatternCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

@Autonomous(name = "Big Launch Zone Red Auto")
public class LargeLaunchZoneRedRoute extends OpMode {
    RobotBase robotBase;
    Follower follower;
    SequentialCommandGroup route;
    AutoTransferAndLaunchCommandGroup autoTransferAndLaunchCommandGroup;
    Pose startPose = new Pose(111.62, 135.55, Math.toRadians(180));
    Pose parkPose = new Pose(127, 95, Math.toRadians(0));
    Pose launchPose = new Pose(90, 95, Math.toRadians(0));
    Pose startToLaunchControl = new Pose(89.321, 136.355, Math.toRadians(0));
    Pose launchToTopRowControl = new Pose(79, 84, Math.toRadians(0));
    Pose preIntakeTopRow = new Pose(104, 84, Math.toRadians(0));
    Pose intakeTopRow = new Pose(126, 84, Math.toRadians(0));
    Pose topRowToLaunchControl = new Pose(90.9, 78.23, Math.toRadians(0));
    Pose launchToMiddleRow = new Pose(74.000, 62.000, Math.toRadians(0));
    Pose preIntakeMiddleRow = new Pose(103.000, 60, Math.toRadians(0));
    Pose intakeMiddleRow = new Pose(127.497, 60, Math.toRadians(0));
    Pose middleRowToLaunchControl = new Pose(79.604, 54.688, Math.toRadians(0));
    Pose launchToBottomRowControl = new Pose(77.016, 85.753, Math.toRadians(0));
    Pose preIntakeBottomRow = new Pose(104.178, 35, Math.toRadians(0));
    Pose intakeBottomRow = new Pose(127, 35, Math.toRadians(0));
    Pose bottomRowToLaunchControl = new Pose(99.020, 40.449);
    PathChain goesFromWallToShootPreload;
    PathChain linesUpWithFurthestRow;
    PathChain intakesArtifacts;
    PathChain goesToShoot;
    PathChain linesUpWithMiddleRow;
    PathChain intakesMiddleRow;
    PathChain goesToShootArtifacts;
    PathChain linesUpToIntakeThirdRow;
    PathChain intakesThirdRow;
    PathChain shootsArtifacts;
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
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(45))
                .build();

        //Closest line//
        linesUpToIntakeThirdRow = follower.pathBuilder()
                .addPath(new BezierCurve(launchPose, launchToTopRowControl, preIntakeTopRow))
                .setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(0))
                .build();
        intakesThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(preIntakeTopRow, intakeTopRow))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
        shootsArtifacts = follower.pathBuilder()
                .addPath(
                        new BezierCurve(intakeTopRow, topRowToLaunchControl, launchPose))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(135))
                .build();

        //Middle line//
        linesUpWithMiddleRow = follower.pathBuilder()
                .addPath(
                        new BezierCurve(launchPose, launchToMiddleRow, preIntakeMiddleRow))
                .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(0))
                .build();
        intakesMiddleRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(preIntakeMiddleRow, intakeMiddleRow))
                .setConstantHeadingInterpolation(0)
                .build();
        goesToShootArtifacts = follower.pathBuilder()
                .addPath(
                        new BezierCurve(intakeMiddleRow, middleRowToLaunchControl, launchPose))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(135))
                .build();

        //furthest line//
        linesUpWithFurthestRow = follower.pathBuilder()
                .addPath(
                        new BezierCurve(launchPose, launchToBottomRowControl, preIntakeBottomRow))
                .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(0))
                .build();
        intakesArtifacts = follower.pathBuilder()
                .addPath(
                        new BezierLine(preIntakeBottomRow, intakeBottomRow))
                .setConstantHeadingInterpolation(0)
                .build();
        goesToShoot = follower.pathBuilder()
                .addPath(
                        new BezierCurve(intakeBottomRow, bottomRowToLaunchControl, launchPose))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(135))
                .build();

        park = follower.pathBuilder()
                .addPath(new BezierLine(launchPose, parkPose))
                .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))
                .build();

        follower = Constants.createFollower(hardwareMap);

        routeMiddleRow = new SequentialCommandGroup(
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower, linesUpWithMiddleRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, intakesMiddleRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(0)),
                new FollowPath(follower, goesToShootArtifacts, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new LaunchCommandGroup(robotBase),
                //new InstantCommand(()->patternCommandGroup.schedule()),
                //new WaitUntilCommand(()->patternCommandGroup.isFinished()),
                new InstantCommand(()->middleRowDone = true));

        routeBottomRow = new SequentialCommandGroup(
                new FollowPath(follower, linesUpWithFurthestRow, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, intakesArtifacts, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(0)),
                new FollowPath(follower, goesToShoot, true, 1),
                //new LaunchCommandGroup(robotBase),
                //new InstantCommand(()->patternCommandGroup.schedule()),
                //new WaitUntilCommand(()->patternCommandGroup.isFinished()),
                new InstantCommand(()->bottomRowDone = true)
        );

        route = new SequentialCommandGroup(
                new WaitUntilCommand(()->(secondsToWait) <= timer.milliseconds()),
                new FollowPath(follower, goesFromWallToShootPreload, true, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new AutoTransferAndLaunchCommandGroup(robotBase, 1750),
                new WaitCommand(3000),
                new FollowPath(follower, linesUpToIntakeThirdRow, true, 1),
                //new WaitUntilCommand(()->autoTransferAndLaunchCommandGroup.isFinished()),
                new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, intakesThirdRow, true, 1));/*,
                new WaitUntilCommand(()->!follower.isBusy()),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(0)),
                new FollowPath(follower, shootsArtifacts, false, 1),
                new WaitUntilCommand(()->!follower.isBusy()),
                //new LaunchCommandGroup(robotBase),
                //new InstantCommand(()->patternCommandGroup.schedule()),
                //new WaitUntilCommand(()->patternCommandGroup.isFinished()),
                new ConditionalCommand(new InstantCommand(()->routeMiddleRow.schedule()), new InstantCommand(()->follower.followPath(park)),()-> (desiredRows == DesiredRows.TWO || desiredRows == DesiredRows.THREE)),
                new WaitUntilCommand(()-> middleRowDone),
                new ConditionalCommand(new InstantCommand(()->routeBottomRow.schedule()), new InstantCommand(()->follower.followPath(park)), ()-> (desiredRows == DesiredRows.THREE)),
                new WaitUntilCommand(()->bottomRowDone),
                new FollowPath(follower, park, true, 1));*/

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
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
        CommandScheduler.getInstance().schedule(route);
        robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.OBELISK);
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
    }
}
