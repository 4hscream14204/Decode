package org.firstinspires.ftc.teamcode.pedropathing.routes;

import static org.firstinspires.ftc.teamcode.pedropathing.poses.PedroRoutes.goesFromWallToShootPreload;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedropathing.poses.PedroRoutes;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;

@Autonomous(name = "Big Launch Zone Auto")
public class LargeLaunchZoneRoute extends OpMode {
    Follower follower;
    SequentialCommandGroup route;
    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        route = new SequentialCommandGroup(
                new FollowPath(follower, goesFromWallToShootPreload, true, 1)
        );
    }

    @Override
    public void loop() {

    }
}
