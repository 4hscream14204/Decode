package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;

@Autonomous(name = "Small Zone Red Park")
public class SmallZoneRedPark extends OpMode {
    Follower follower;
    SequentialCommandGroup route;
    PathChain Path1;
    Pose startPose = new Pose(88, 8, Math.toRadians(0));

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        Path1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(startPose, new Pose(115.643, 10)))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        route = new SequentialCommandGroup(
                new FollowPath(follower, Path1, true, 1)
        );
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
    }

    @Override
    public void loop() {
        follower.update();
    }

    @Override
    public void stop() {
        DataStorage.endPosition = follower.getPose();
        DataStorage.alliance = DecodeEnums.Alliance.RED;
    }
}
