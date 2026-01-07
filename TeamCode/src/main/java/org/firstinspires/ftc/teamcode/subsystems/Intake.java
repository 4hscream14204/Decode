package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Intake extends SubsystemBase {

    public DcMotor intakeMotor;

    public Intake(DcMotor m_intakeMotor) {
        intakeMotor = m_intakeMotor;
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void intake(double m_Power) {
        intakeMotor.setPower(m_Power);
    }
}
