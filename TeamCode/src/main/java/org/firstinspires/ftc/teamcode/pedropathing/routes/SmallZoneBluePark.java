package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;

@Autonomous(name = "Small Zone Blue Park")
public class SmallZoneBluePark extends OpMode {
    Follower follower;
    SequentialCommandGroup route;
    PathChain Path1;
    Pose startPose = new Pose(88, 8, Math.toRadians(0)).mirror();

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        Path1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(startPose, new Pose(115.643, 11).mirror()))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        route = new SequentialCommandGroup(
                new FollowPath(follower, Path1, true, 1)
        );
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
        CommandScheduler.getInstance().schedule(route);
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        follower.update();
    }

    @Override
    public void stop() {
        DataStorage.endPosition = follower.getPose();
        DataStorage.alliance = DecodeEnums.Alliance.BLUE;
    }
}
