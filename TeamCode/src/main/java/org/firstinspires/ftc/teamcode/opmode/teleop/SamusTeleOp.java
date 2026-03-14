package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.RobotBase;

@TeleOp(name = "Samus TeleOp")
public class SamusTeleOp extends OpMode {
    RobotBase robotBase;
    @Override
    public void init() {
        robotBase = new RobotBase(hardwareMap);
    }

    @Override
    public void loop() {

    }
}
