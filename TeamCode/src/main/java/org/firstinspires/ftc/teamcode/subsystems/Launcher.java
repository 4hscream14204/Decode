package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Launcher extends SubsystemBase {
    public DcMotorEx launcherMotor;
    public final double dblLaunchWheelRadius = 1.375;
    public final double launchVar1 = 2.2787;
    public final double launchVar2 = 1770.4;

    public Launcher(DcMotorEx m_Launcher){
        launcherMotor = m_Launcher;
        launcherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherMotor.setVelocityPIDFCoefficients(9, 0.8, 0, 0.7);
    }

    public void setPower(double power){
        launcherMotor.setPower(power);
    }

    public void setVelocity(double m_velocity) {
        launcherMotor.setVelocity(m_velocity);
    }

    /*public void setRPM(double m_RPM) {
        double m_RPMToVelocity = m_RPM / (6.28 * dblLaunchWheelRadius);
        launcherMotorLeft.setVelocity(m_RPMToVelocity);
    }*/

    public double getVelocity(){
        return launcherMotor.getVelocity();
    }

    /*public double getRPM() {
        return launcherMotorLeft.getVelocity() * (6.28 * dblLaunchWheelRadius);
    }*/

    public void setLaunchVelocity(double m_Distance) {

        double velocity = getLaunchVelocity(m_Distance);
        setVelocity(velocity);
    }

    public double getLaunchVelocity(double m_Distance){
        return ((0.0098*(Math.pow(m_Distance, 2))) - (2.7572 * m_Distance) + 2088.2/*1938.2*/);
    }
}
