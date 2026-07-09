package org.firstinspires.ftc.teamcode.opmode.auto.cri;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import com.seattlesolvers.solverslib.pedroCommand.HoldPointCommand;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.commands.AutoTurretHeadingCommand;
import org.firstinspires.ftc.teamcode.commands.DynamicVelocityAutoCommand;
import org.firstinspires.ftc.teamcode.commands.ToggleBottomSpikeOrderCommand;
import org.firstinspires.ftc.teamcode.commands.ToggleCurrentSpikeOrderCommand;
import org.firstinspires.ftc.teamcode.commands.ToggleMiddleSpikeOrderCommand;
import org.firstinspires.ftc.teamcode.commands.ToggleTopSpikeOrderCommand;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;


@Autonomous(name = "Blue Prism Auto")
public class BluePrismAuto extends OpMode {

    Follower follower;
    RobotBase robotBase;
    SequentialCommandGroup path;
    SequentialCommandGroup idlePath;
    SequentialCommandGroup topPath;
    SequentialCommandGroup middlePath;
    SequentialCommandGroup bottomPath;
    SequentialCommandGroup parkingPath;
    int artifactsInBotCount;
    GamepadEx gamepad;
    ElapsedTime timer;
    public enum SpikeOrder{
        NONE,
        FIRST,
        SECOND,
        THIRD,
        PARK
    }
   public static SpikeOrder currentSpikeOrder = SpikeOrder.NONE;
    public static SpikeOrder topSpikeOrder = SpikeOrder.NONE;
    public static SpikeOrder middleSpikeOrder = SpikeOrder.NONE;
    public static SpikeOrder bottomSpikeOrder = SpikeOrder.NONE;
    int waitTime= 0;

    Pose startPose = new Pose(62, 185, Math.toRadians(-90));//62,185

    Pose launchPose1 = new Pose(68, 121, Math.toRadians(-135));
    Pose topLineUpPose = new Pose( 39, 108, Math.toRadians(180));
    Pose middleLineUpPose = new Pose(42,86, Math.toRadians(180));
    Pose bottomLineUpPose = new Pose(34,61, Math.toRadians(180));
    Pose topSpikePose = new Pose(6, 108, Math.toRadians(180));
    Pose middleSpikePose = new Pose(6, 85, Math.toRadians(180));
    Pose bottomSpikePose = new Pose(6, 61, Math.toRadians(180));
//    Pose launchPose2 = new Pose(75, 111, Math.toRadians(225));
    Pose parkPose = new Pose(75,158, Math.toRadians(-90));

    BezierLine startToLaunchPath = new BezierLine(startPose, launchPose1);
    BezierLine basicParkPath = new BezierLine(launchPose1, parkPose);
    BezierLine topLineUpPath = new BezierLine(launchPose1, topLineUpPose);
    BezierLine middleLineUpPath = new BezierLine(launchPose1, middleLineUpPose);
    BezierLine bottomLineUpPath = new BezierLine(launchPose1, bottomLineUpPose);
    BezierLine topSpikePath = new BezierLine(topLineUpPose, topSpikePose);
    BezierLine middleSpikePath = new BezierLine(middleLineUpPose, middleSpikePose);
    BezierLine bottomSpikePath = new BezierLine(bottomLineUpPose, bottomSpikePose);
    BezierLine topBackUpPath = new BezierLine(topSpikePose, topLineUpPose);
    BezierLine middleBackUpPath = new BezierLine(middleSpikePose, middleLineUpPose);
    BezierLine bottomBackUpPath = new BezierLine(bottomSpikePose, bottomLineUpPose);
    BezierLine launchFromTopPath = new BezierLine(topLineUpPose, launchPose1);
    BezierLine launchFromMiddlePath = new BezierLine(middleLineUpPose, launchPose1);
    BezierLine launchFromBottomPath = new BezierLine(bottomLineUpPose, launchPose1);
    BezierLine parkPath = new BezierLine(launchPose1, parkPose);

    PathChain startLaunch;
    PathChain middleLineUp;
    PathChain middleSpike;
    PathChain middleBackUp;
    PathChain middleSpikeLaunch;
    PathChain bottomLineUp;
    PathChain bottomSpike;
    PathChain bottomBackUp;
    PathChain bottomSpikeLaunch;
    PathChain topLineUp;
    PathChain topSpike;
    PathChain topBackUp;
    PathChain topSpikeLaunch;
    PathChain park;
    PathChain basicPark;
    PathChain spikeReady;



    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        follower = Constants.createFollower(hardwareMap);
        robotBase = new RobotBase(hardwareMap);
        gamepad = new GamepadEx(gamepad1);
        timer = new ElapsedTime();

        startLaunch = follower.pathBuilder()
                .addPath(startToLaunchPath)
                .setLinearHeadingInterpolation(startPose.getHeading(), Math.toRadians(-135))
                //.addParametricCallback(0.97, ()->CommandScheduler.getInstance().schedule(new WaitCommand(1000)))
                .addParametricCallback(0.25, ()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.2))))
                .addParametricCallback(0.95, ()->CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new WaitCommand(500),
                                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
                                new WaitCommand(800),
                                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer(0.2)),
                                new InstantCommand(()->robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                                new WaitCommand(600),
                                new InstantCommand(()->currentSpikeOrder = SpikeOrder.FIRST)
                )))
                .build();

//        spikeReady = follower.pathBuilder()
//                .addPath(spikeReadyPath)
//                .setLinearHeadingInterpolation(launchPose1.getHeading(), launchPose2.getHeading())
//                .build();

        basicPark = follower.pathBuilder()
                .addPath(basicParkPath)
                .setLinearHeadingInterpolation(launchPose1.getHeading(), parkPose.getHeading())
                .build();

        middleLineUp = follower.pathBuilder()
                .addPath(middleLineUpPath)
                .setLinearHeadingInterpolation(launchPose1.getHeading(), Math.toRadians(-180))
                .build();

        middleSpike = follower.pathBuilder()
                .addPath(middleSpikePath)
                .setConstantHeadingInterpolation(Math.toRadians(-180))
                .addParametricCallback(0, ()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE))
                ))
                .addParametricCallback(0, ()->robotBase.intakeTransferSubsystem.intake())
                .build();

        middleBackUp = follower.pathBuilder()
                .addPath(middleBackUpPath)
                .setConstantHeadingInterpolation(Math.toRadians(-180))
                .addParametricCallback(0.85,()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK))
                ))
                .addPath(launchFromMiddlePath)
                .setLinearHeadingInterpolation(middleLineUpPose.getHeading(), Math.toRadians(225))
                .addParametricCallback(0.99, ()->CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new WaitCommand(500),
                                new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
                                new WaitCommand(800),
                                new InstantCommand(() -> robotBase.intakeTransferSubsystem.intakeAndTransfer(0.2)),
                                new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                                new WaitCommand(600),
                                new InstantCommand(()->CommandScheduler.getInstance().schedule(new ToggleCurrentSpikeOrderCommand()))
                        )))
                .build();

        bottomLineUp = follower.pathBuilder()
                .addPath(bottomLineUpPath)
                .setLinearHeadingInterpolation(launchPose1.getHeading(), bottomLineUpPose.getHeading())
                .build();

        bottomSpike = follower.pathBuilder()
                .addPath(bottomSpikePath)
                .setLinearHeadingInterpolation(bottomLineUpPose.getHeading(), bottomSpikePose.getHeading())
                .addParametricCallback(0, ()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE))
                ))
                .addParametricCallback(0, ()->robotBase.intakeTransferSubsystem.intake())
                .build();

        bottomBackUp = follower.pathBuilder()
                .addPath(bottomBackUpPath)
                .setLinearHeadingInterpolation(bottomSpikePose.getHeading(), bottomLineUpPose.getHeading())
                .addParametricCallback(0.85,()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK))

                ))
                .addPath(launchFromBottomPath)
                .setLinearHeadingInterpolation(bottomLineUpPose.getHeading(), launchPose1.getHeading())
                .addParametricCallback(0.99, ()->CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new WaitCommand(500),
                                new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
                                new WaitCommand(800),
                                new InstantCommand(() -> robotBase.intakeTransferSubsystem.intakeAndTransfer(0.2)),
                                new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                                new WaitCommand(600),
                                new InstantCommand(()->CommandScheduler.getInstance().schedule(new ToggleCurrentSpikeOrderCommand()))
                        )))
                .build();


        topLineUp = follower.pathBuilder()
                .addPath(topLineUpPath)
                .setLinearHeadingInterpolation(launchPose1.getHeading(), topLineUpPose.getHeading())
                .build();

        topSpike = follower.pathBuilder()
                .addPath(topSpikePath)
                .setLinearHeadingInterpolation(topLineUpPose.getHeading(), topSpikePose.getHeading())
                .addParametricCallback(0, ()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE))
                ))
                .addParametricCallback(0, ()->robotBase.intakeTransferSubsystem.intake())
                .build();

        topBackUp = follower.pathBuilder()
                .addPath(topBackUpPath)
                .setLinearHeadingInterpolation(topSpikePose.getHeading(), topLineUpPose.getHeading())
                .addPath(launchFromTopPath)
                .setLinearHeadingInterpolation(topLineUpPose.getHeading(), launchPose1.getHeading())
                .addParametricCallback(0.85,()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.BLOCK))
                ))

                .addParametricCallback(0.99, ()->CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new WaitCommand(500),
                                new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
                                new WaitCommand(800),
                                new InstantCommand(() -> robotBase.intakeTransferSubsystem.intakeAndTransfer(0.2)),
                                new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                                new WaitCommand(600),
                                new InstantCommand(()->CommandScheduler.getInstance().schedule(new ToggleCurrentSpikeOrderCommand()))
                        )))
                .build();

        park = follower.pathBuilder()
                .addPath(parkPath)
                .setLinearHeadingInterpolation(launchPose1.getHeading(), parkPose.getHeading())
                .build();

        path = new SequentialCommandGroup(
                new WaitUntilCommand(()->waitTime <= timer.milliseconds()),
                new FollowPathCommand(follower, startLaunch, true,1)
               );

        idlePath = new SequentialCommandGroup(
                new FollowPathCommand(follower, basicPark, true, 1)
        );

        topPath = new SequentialCommandGroup(
                new WaitUntilCommand(()->!follower.isBusy()),
                new WaitUntilCommand(()->currentSpikeOrder == topSpikeOrder),
                new FollowPathCommand(follower, topLineUp, true, 1),
                new FollowPathCommand(follower, topSpike, true, 1),
                new FollowPathCommand(follower, topBackUp, true, 1)
        );
//                new FollowPathCommand(follower, park, true, 1)

        middlePath = new SequentialCommandGroup(
                new WaitUntilCommand(()->!follower.isBusy()),
                new WaitUntilCommand(()->currentSpikeOrder == middleSpikeOrder),
                new FollowPathCommand(follower, middleLineUp, true, 1),
                new FollowPathCommand(follower, middleSpike, true, 1),
                new FollowPathCommand(follower, middleBackUp, true, 1)
//                new FollowPathCommand(follower, park, true, 1)
        );

        bottomPath = new SequentialCommandGroup(
                new WaitUntilCommand(()->!follower.isBusy()),
                new WaitUntilCommand(()->currentSpikeOrder == bottomSpikeOrder),
                new FollowPathCommand(follower, bottomLineUp, true, 1),
                new FollowPathCommand(follower, bottomSpike, true, 1),
                new FollowPathCommand(follower, bottomBackUp, true, 1)
//                new FollowPathCommand(follower, park, true, 1)
        );

        parkingPath = new SequentialCommandGroup(
                new WaitUntilCommand(()->!follower.isBusy()),
                new WaitUntilCommand(()->currentSpikeOrder == SpikeOrder.PARK),
                new FollowPathCommand(follower, park, true, 1)
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

        gamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->waitTime += 1000)));

        gamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->waitTime -= 1000)));

        gamepad.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new ToggleTopSpikeOrderCommand()));

        gamepad.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new ToggleMiddleSpikeOrderCommand()));

        gamepad.getGamepadButton(GamepadKeys.Button.SQUARE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new ToggleBottomSpikeOrderCommand()));
    }

    @Override
    public void init_loop() {
        CommandScheduler.getInstance().run();
        gamepad.readButtons();
        telemetry.addData("Wait Time", waitTime);
        telemetry.addData("Top Spike", topSpikeOrder);
        telemetry.addData("Middle Spike", middleSpikeOrder);
        telemetry.addData("Bottom Spike", bottomSpikeOrder);
        telemetry.addData("Current Spike Order", currentSpikeOrder);
    }

    @Override
    public void start() {
        follower.setStartingPose(new Pose(startPose.getX(), startPose.getY(), Math.toRadians(-90)));
        CommandScheduler.getInstance().schedule(new WaitCommand(waitTime));
        CommandScheduler.getInstance().schedule(path);

        if (middleSpikeOrder == SpikeOrder.FIRST){
            CommandScheduler.getInstance().schedule(middlePath);
        } else if (bottomSpikeOrder == SpikeOrder.FIRST) {
            CommandScheduler.getInstance().schedule(bottomPath);
        } else if (topSpikeOrder == SpikeOrder.FIRST) {
            CommandScheduler.getInstance().schedule(topPath);
        } else {
            currentSpikeOrder = SpikeOrder.SECOND;
        }

        if (middleSpikeOrder == SpikeOrder.SECOND){
            CommandScheduler.getInstance().schedule(middlePath);
        } else if (bottomSpikeOrder == SpikeOrder.SECOND) {
            CommandScheduler.getInstance().schedule(bottomPath);
        } else if (topSpikeOrder == SpikeOrder.SECOND) {
            CommandScheduler.getInstance().schedule(topPath);
        } else{
            currentSpikeOrder = SpikeOrder.THIRD;
        }

        if (middleSpikeOrder == SpikeOrder.THIRD){
            CommandScheduler.getInstance().schedule(middlePath);
        } else if (bottomSpikeOrder == SpikeOrder.THIRD) {
            CommandScheduler.getInstance().schedule(bottomPath);
        } else if (topSpikeOrder == SpikeOrder.THIRD) {
            CommandScheduler.getInstance().schedule(topPath);
        }
        else {
            currentSpikeOrder = SpikeOrder.PARK;
        }
        CommandScheduler.getInstance().schedule(parkingPath);
        CommandScheduler.getInstance().schedule(new AutoTurretHeadingCommand(robotBase, follower, DataStorage.blueGoalPose));
        CommandScheduler.getInstance().schedule(new DynamicVelocityAutoCommand(robotBase, follower));
        robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE);
        timer.reset();
    }

    @Override
    public void loop() {
        follower.update();
        //robotBase.hoodSubsystem.setDynamicPosition(follower.getPose().distanceFrom(goalPose));

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

