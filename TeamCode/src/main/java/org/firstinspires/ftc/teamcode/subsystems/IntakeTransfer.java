package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

public class IntakeTransfer {
    public DcMotor intakeMotor;
    public DcMotor transferMotor;

    public IntakeTransfer(DcMotor m_intakeMotor, DcMotor m_transferMotor) {
        intakeMotor = m_intakeMotor;
        transferMotor = m_transferMotor;
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transferMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setPower(double m_Power){
        intakeMotor.setPower(m_Power);
    }

    public void intake(double m_Power) {
        setPower(m_Power);
    }

    public void intake(){
        setPower(-1);
        transfer(0.75);
    }

    public void outtake(){
        setPower(1);
        transfer(-0.75);
    }

    public void transfer(double m_power){
        transferMotor.setPower(m_power);
    }

    public void intakeAndTransfer(){
        intake(-0.8);
        transfer(0.8);
    }

    public void intakeAndTransfer(double power){
        intake(-power);
        transfer(power);
    }

    public void intakeAndTransferOuttake(){
        outtake();
        transfer(-1);
    }

    public void stopAll(){
        intakeMotor.setPower(0);
        transferMotor.setPower(0);
    }
}
