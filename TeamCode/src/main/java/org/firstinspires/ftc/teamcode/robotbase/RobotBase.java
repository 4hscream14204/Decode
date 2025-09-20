package org.firstinspires.ftc.teamcode.robotbase;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class RobotBase {

        public Intake intakeSubsystem;

        public RobotBase(HardwareMap hwMap) {

            intakeSubsystem = new Intake(hwMap.dcMotor.get("intake_motor"));
        }
}
