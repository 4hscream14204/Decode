package org.firstinspires.ftc.teamcode.robotbase;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

public class RobotBase {

        public Intake intakeSubsystem;
        public Launcher launcherSubsystem;

        public RobotBase(HardwareMap hwMap) {

            intakeSubsystem = new Intake(hwMap.dcMotor.get("intake_motor"));
            launcherSubsystem = new Launcher(hwMap.get(DcMotorEx.class, "launcherMotor"));
        }
}
