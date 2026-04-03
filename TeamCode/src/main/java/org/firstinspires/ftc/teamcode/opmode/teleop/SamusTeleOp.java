package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;

@TeleOp(name = "Samus TeleOp")
public class SamusTeleOp extends OpMode {
    RobotBase robotBase;
    Follower follower;
    GamepadEx mainController;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        mainController = new GamepadEx(gamepad1);
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        mainController.readButtons();
        follower.update();
    }
}
