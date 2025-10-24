package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Launcher extends SubsystemBase {
    public DcMotorEx launcherMotor;

    public final double dblLaunchWheelRadius = 1.375;
    public final double launchVar1 = 5.7879;
    public final double launchVar2 = 1770.4;

    public Launcher(DcMotorEx m_Launcher){
        launcherMotor = m_Launcher;
        launcherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setPower(double power){
        launcherMotor.setPower(power);
    }

    public void setVelocity(double m_velocity) {
        launcherMotor.setVelocity(m_velocity);
    }

    public void setRPM(double m_RPM) {
        double m_RPMToVelocity = m_RPM / (6.28 * dblLaunchWheelRadius);
        launcherMotor.setVelocity(m_RPMToVelocity);
    }

    public double getRPM() {
        return launcherMotor.getVelocity() * (6.28 * dblLaunchWheelRadius);
    }

    public void setLaunchVelocity(double m_Distance) {
        double velocity = 0 - ((launchVar1 * m_Distance) + launchVar2);
        setVelocity(velocity);
    }
}
