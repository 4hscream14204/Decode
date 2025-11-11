package org.firstinspires.ftc.teamcode.base;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Launcher;

public class RobotBase {
    public Launcher launcherSubsystem;

    public RobotBase(HardwareMap hardwareMap){
        launcherSubsystem = new Launcher(hardwareMap.get(DcMotorEx.class, "launcherMotor"));
    }
}
