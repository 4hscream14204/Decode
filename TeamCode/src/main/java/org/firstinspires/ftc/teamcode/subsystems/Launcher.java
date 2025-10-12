package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Launcher extends SubsystemBase {
    public DcMotorEx launcherMotor;

    public final double dblLaunchWheelRadius = 1.375;

    public Launcher(DcMotorEx m_Launcher){
        launcherMotor = m_Launcher;
        launcherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setPower(double power){
        launcherMotor.setPower(power);
    }

    public void setRPM(double m_RPM) {
        double m_RPMToVelocity = m_RPM / (6.28 * dblLaunchWheelRadius);
        launcherMotor.setVelocity(m_RPMToVelocity);
    }

    public double getRPM() {
        return launcherMotor.getVelocity() * (6.28 * dblLaunchWheelRadius);
    }
}
