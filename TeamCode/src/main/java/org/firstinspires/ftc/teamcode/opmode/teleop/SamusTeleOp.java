package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;

@TeleOp(name = "Samus TeleOp")
public class SamusTeleOp extends OpMode {
    RobotBase robotBase;
    Follower follower;
    @Override
    public void init() {
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
    }

    @Override
    public void loop() {
        follower.update();
    }
}
