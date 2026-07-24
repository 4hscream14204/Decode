package org.firstinspires.ftc.teamcode.opmode.auto.cri;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.commands.AutoTurretHeadingCommand;
import org.firstinspires.ftc.teamcode.commands.DynamicVelocityCommand;
import org.firstinspires.ftc.teamcode.commands.TurretHeadingControlCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.TransferBlocker;

@Autonomous(name = "Red Prism Only Preload")
public class RedPrismAutoPreload extends OpMode {

    double fieldWidth = 189.5;

    Pose startPose = new Pose(62, 185, Math.toRadians(-90)).mirror(fieldWidth);//62,185
    Pose launchPose1 = new Pose(68, 121, Math.toRadians(-135)).mirror(fieldWidth);
    Pose parkPose = new Pose(75,158, Math.toRadians(-90)).mirror(fieldWidth);

    BezierLine startToLaunch = new BezierLine(startPose, launchPose1);
    BezierLine launchToPark = new BezierLine(launchPose1, parkPose);

    PathChain launch;
    PathChain park;

    Follower follower;
    RobotBase robotBase;
    SequentialCommandGroup path;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        robotBase = new RobotBase(hardwareMap);
        DataStorage.alliance = DecodeEnums.Alliance.RED;
        robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK);

        launch = follower.pathBuilder()
                .addPath(startToLaunch)
                .setLinearHeadingInterpolation(startPose.getHeading(), launchPose1.getHeading())
                .build();

        park = follower.pathBuilder()
                .addPath(launchToPark)
                .setLinearHeadingInterpolation(launchPose1.getHeading(), parkPose.getHeading())
                .build();

        path = new SequentialCommandGroup(
                new FollowPathCommand(follower, launch, true, 1),
                new WaitCommand(500),
                new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.RELEASE)),
                new InstantCommand(()->robotBase.intakeTransferSubsystem.intakeAndTransfer()),
                new WaitCommand(800),
                new InstantCommand(() -> robotBase.intakeTransferSubsystem.intakeAndTransfer(0.2)),
                new InstantCommand(() -> robotBase.transferBlockerSubsystem.setPosition(TransferBlocker.TransferBlockerPosition.BLOCK)),
                new FollowPathCommand(follower, park, true, 1)
        );
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
        path.schedule();
        CommandScheduler.getInstance().schedule(new AutoTurretHeadingCommand(robotBase, follower, DataStorage.redGoalPose));
        CommandScheduler.getInstance().schedule(new DynamicVelocityCommand(robotBase, follower));
    }

    @Override
    public void loop() {
        follower.update();
        CommandScheduler.getInstance().run();
    }

    @Override
    public void stop() {
        DataStorage.endPosition = follower.getPose();
        DataStorage.alliance = DecodeEnums.Alliance.RED;
    }
}
