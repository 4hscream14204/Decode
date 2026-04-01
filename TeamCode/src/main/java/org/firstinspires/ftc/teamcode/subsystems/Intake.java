package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Intake {
    public DcMotor intakeMotor;
    public DcMotor intakeMotor2;

    public Intake(DcMotor m_intakeMotor, DcMotor m_intakeMotor2) {
        intakeMotor = m_intakeMotor;
        intakeMotor2 = m_intakeMotor2;
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setPower(double m_Power){
        intakeMotor.setPower(m_Power);
        //intakeMotor2 might need to be reversed in the future
        intakeMotor2.setPower(m_Power);
    }

    public void intake(double m_Power) {
        setPower(m_Power);
    }

    public void intake(){
        setPower(1);
    }

    public void outtake(){
        setPower(-1);
    }
}
