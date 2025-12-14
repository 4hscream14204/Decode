package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
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
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferPatternCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.CameraLight;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;


@Autonomous(name = "SmallBlueAuto")
public class SmallLaunchZoneBlueRoute extends OpMode {
    Follower follower;
    RobotBase robotBase;
    TransferPatternCommandGroup transferPatternCommandGroup;
    SequentialCommandGroup route;
    SequentialCommandGroup routeMiddleRow;
    SequentialCommandGroup routeTopRow;
    SequentialCommandGroup routePark;
    LaunchCommandGroup launchArtifacts;
    GamepadEx chassis;
    ElapsedTime timer;
    DesiredRows desiredRows = DesiredRows.THREE;
    boolean middleRowDone = false;
    boolean topRowDone = false;
    int secondsToWait = 0;


    Pose startPose = new Pose(88, 8, Math.toRadians(90)).mirror();
    Pose launchPose = new Pose(87, 14, Math.toRadians(65)).mirror();
    Pose nextToFirstRow = new Pose(104, 34, Math.toRadians(0)).mirror();
    Pose intakeFirstRow = new Pose(135, 34, Math.toRadians(0)).mirror();
    Pose nextToSecondRow = new Pose(105, 59, Math.toRadians(0)).mirror();
    Pose intakeSecondRow = new Pose(135, 59, Math.toRadians(0)).mirror();
    Pose nextToThirdRow = new Pose(104, 84, Math.toRadians(0)).mirror();
    Pose intakeThirdRow = new Pose(130, 84, Math.toRadians(0)).mirror();
    Pose parkPose = new Pose(106, 74, Math.toRadians(270)).mirror();

    public enum DesiredRows{
        ONE,
        TWO,
        THREE
    }

    @Override
    public void init() {
        DataStorage.alliance = DecodeEnums.Alliance.RED;
        chassis = new GamepadEx(gamepad1);
        timer = new ElapsedTime();
        robotBase = new RobotBase(hardwareMap);
        robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.OBELISK);

        chassis.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new InstantCommand(() -> desiredRows = DesiredRows.THREE)));

        chassis.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new InstantCommand(() -> desiredRows = DesiredRows.TWO)));

        chassis.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new InstantCommand(() -> desiredRows = DesiredRows.ONE)));

        chassis.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new InstantCommand(() -> secondsToWait = secondsToWait + 1000)));

        chassis.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new InstantCommand(() -> secondsToWait = secondsToWait - 1000)));

        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);

        PathChain launchPreload = follower.pathBuilder()
                .addPath(
                        new BezierLine(startPose, launchPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), launchPose.getHeading())
                .build();

        PathChain lineUpToIntake = follower.pathBuilder()
                .addPath(
                        new BezierLine(launchPose, nextToThirdRow))
                .setLinearHeadingInterpolation(launchPose.getHeading(), nextToThirdRow.getHeading())
                .build();

        PathChain intakeFurthestRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(nextToThirdRow, intakeThirdRow))
                .setConstantHeadingInterpolation(nextToThirdRow.getHeading())
                .build();

        PathChain thirdTimeGoingToShoot = follower.pathBuilder()
                .addPath(
                        new BezierLine(intakeThirdRow, launchPose))
                .setLinearHeadingInterpolation(intakeThirdRow.getHeading(), launchPose.getHeading())
                .build();

        PathChain lineUpToIntakeSecondRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(launchPose, nextToSecondRow))
                .setLinearHeadingInterpolation(launchPose.getHeading(), nextToSecondRow.getHeading())
                .build();

        PathChain intakeMiddleRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(nextToSecondRow, intakeSecondRow))
                .setConstantHeadingInterpolation(nextToSecondRow.getHeading())
                .build();

        PathChain secondTimeGoingToShoot = follower.pathBuilder()
                .addPath(
                        new BezierLine(intakeSecondRow, launchPose))
                .setLinearHeadingInterpolation(intakeSecondRow.getHeading(), launchPose.getHeading())
                .build();

        PathChain lineUpToThirdRow = follower.pathBuilder()
                .addPath(
                        new BezierLine(launchPose, nextToFirstRow))
                .setLinearHeadingInterpolation(launchPose.getHeading(), nextToFirstRow.getHeading())
                .build();

        PathChain intakeThirdRow = follower.pathBuilder()
                .addPath(new BezierLine(nextToFirstRow, intakeFirstRow))
                .setConstantHeadingInterpolation(intakeFirstRow.getHeading())
                .build();

        PathChain firstTimeGoingToShoot = follower.pathBuilder()
                .addPath(new BezierLine(intakeFirstRow, launchPose))
                .setLinearHeadingInterpolation(intakeFirstRow.getHeading(), launchPose.getHeading())
                .build();

        PathChain park = follower.pathBuilder()
                .addPath(new BezierLine(launchPose, parkPose))
                .setLinearHeadingInterpolation(launchPose.getHeading(), parkPose.getHeading())
                .build();

        routeMiddleRow = new SequentialCommandGroup(
                new FollowPath(follower, lineUpToIntakeSecondRow, true, 1),
                new FollowPath(follower, intakeMiddleRow, true, 1),
                new FollowPath(follower, secondTimeGoingToShoot, true, 1),
                new InstantCommand(() -> middleRowDone = true)
        );

        routeTopRow = new SequentialCommandGroup(
                new FollowPath(follower, lineUpToIntake, true, 1),
                new FollowPath(follower, intakeFurthestRow, true, 1),
                new FollowPath(follower, thirdTimeGoingToShoot, true, 1),
                new InstantCommand(() -> topRowDone = true)
        );

        routePark = new SequentialCommandGroup(
                new FollowPath(follower, park, true, 1)
        );

        route = new SequentialCommandGroup(
                new TransferResetCommandGroup(robotBase),
                new SetAllVelocityCommandGroup(robotBase, 2065),
                new FollowPath(follower, launchPreload, true, 1),
                new WaitUntilCommand(() -> !follower.isBusy()),
                new AutoTransferAndLaunchCommandGroup(robotBase, 2065),
                // new WaitCommand(1000),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                //  new SetAllVelocityCommandGroup(robotBase, 0),
                new InstantCommand(() -> robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, lineUpToThirdRow, true, 1),
                new WaitUntilCommand(() -> !follower.isBusy()),
                new FollowPath(follower, intakeThirdRow, true, 1),
                new WaitUntilCommand(() -> !follower.isBusy()),
                // new WaitCommand(1000),
                new SetAllVelocityCommandGroup(robotBase, 2065),
                new FollowPath(follower, firstTimeGoingToShoot, true, 1),
                new WaitUntilCommand(() -> !follower.isBusy()),
                new InstantCommand(() -> robotBase.intakeSubsystem.intake(1)),
                new AutoTransferAndLaunchCommandGroup(robotBase, 2050),
                //  new WaitCommand(1000),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                // new SetAllVelocityCommandGroup(robotBase, 0),
                new InstantCommand(() -> robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, lineUpToIntakeSecondRow, true, 1),
                new WaitUntilCommand(() -> !follower.isBusy()),
                new FollowPath(follower, intakeMiddleRow, true, 1),
                new WaitUntilCommand(() -> !follower.isBusy()),
                //new SetAllVelocityCommandGroup(robotBase, 1900),
                new FollowPath(follower, secondTimeGoingToShoot, true, 1),
                new WaitUntilCommand(() -> !follower.isBusy()),
                new InstantCommand(() -> robotBase.intakeSubsystem.intake(1)),
                new AutoTransferAndLaunchCommandGroup(robotBase, 2065),
                // new WaitCommand(1000),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                new SetAllVelocityCommandGroup(robotBase, 0),
                new InstantCommand(() -> robotBase.intakeSubsystem.intake(-1)),
                new FollowPath(follower, lineUpToIntake, true, 1),
                new WaitUntilCommand(() -> !follower.isBusy()),
                new FollowPath(follower, intakeFurthestRow).withTimeout(1500),
                new FollowPath(follower,park),
                new InstantCommand(()-> robotBase.intakeSubsystem.intake(1))
               /*
               intaking last row and shooting
                new WaitUntilCommand(()->!follower.isBusy()),
                new FollowPath(follower,thirdTimeGoingToShoot,true,1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new AutoTransferAndLaunchCommandGroup(robotBase,2000),
                 */

                /*new WaitUntilCommand(()->!follower.isBusy()),
                new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(2000)),
                new FollowPath(follower,thirdTimeGoingToShoot,true,1),
                new WaitUntilCommand(()->!follower.isBusy()),
                new AutoTransferAndLaunchCommandGroup(robotBase,2000),
                //new WaitCommand(1000),
                new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0)),
                new FollowPath(follower,park,true,1)*/

                //new LaunchCommandGroup(robotBase),
                //new InstantCommand(()->transferPatternCommandGroup.schedule()),
                //new WaitUntilCommand(()->transferPatternCommandGroup.isFinished()),
                // new ConditionalCommand(new InstantCommand(()->routeMiddleRow.schedule()), new InstantCommand(()->routePark.schedule()), ()->desiredRows == DesiredRows.TWO || desiredRows == DesiredRows.THREE),
                //new WaitUntilCommand(()->middleRowDone),
                //new ConditionalCommand(new InstantCommand(()->routeTopRow.schedule()), new InstantCommand(()->routePark.schedule()), ()->desiredRows == DesiredRows.THREE),
                //new LaunchCommandGroup(robotBase),
                //new WaitUntilCommand(()->topRowDone),
                //new InstantCommand(()->routePark.schedule())
        );
    }

    @Override
    public void init_loop() {
        chassis.readButtons();
        CommandScheduler.getInstance().run();
        robotBase.limelightSubsystem.updateLimelight();
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
        telemetry.addData("Rows", desiredRows);
        telemetry.update();
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
        CommandScheduler.getInstance().schedule(route);
        robotBase.cameraLightSubsystemRight.setShade(CameraLight.Shades.HALF);
        robotBase.cameraLightSubsystemLeft.setShade(CameraLight.Shades.HALF);
    }

    @Override
    public void loop() {
        robotBase.sorterCameraSubsystem.getAnalysis();
        robotBase.limelightSubsystem.updateLimelight();
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
        CommandScheduler.getInstance().run();
        follower.update();
      telemetry.addData("Rows", desiredRows);
        telemetry.addData("Left", robotBase.sorterCameraSubsystem.getClosestSwatchLeft());
        telemetry.addData("Middle", robotBase.sorterCameraSubsystem.getClosestSwatchMiddle());
        telemetry.addData("Right", robotBase.sorterCameraSubsystem.getClosestSwatchRight());
        telemetry.update();
    }

    @Override
    public void stop() {
        DataStorage.alliance = DecodeEnums.Alliance.RED;
        robotBase.limelightSubsystem.limelight.stop();
        DataStorage.endPosition = follower.getPose();
    }
}
