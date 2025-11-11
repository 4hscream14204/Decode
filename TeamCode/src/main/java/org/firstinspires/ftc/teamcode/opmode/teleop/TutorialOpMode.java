package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.base.RobotBase;

public class TutorialOpMode extends OpMode {
    RobotBase robotBase;
    @Override
    public void init() {
        robotBase = new RobotBase(hardwareMap);
    }

    @Override
    public void loop() {
        if(gamepad1.a){
            robotBase.launcherSubsystem.setVelocity(1000);
        }
        if(gamepad1.b){
            robotBase.launcherSubsystem.setVelocity(2000);
        }
        if(gamepad1.x){
            robotBase.launcherSubsystem.setVelocity(1500);
        }
        if(gamepad1.y){
            robotBase.launcherSubsystem.setVelocity(0);
        }
    }
}
