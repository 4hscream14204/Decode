package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Launcher extends SubsystemBase {
    public DcMotorEx launcherMotorLeft;
    public DcMotorEx launcherMotorRight;

    public final double dblLaunchWheelRadius = 1.375;
    public final double launchVar1 = 2.2787;
    public final double launchVar2 = 1770.4;

    public Launcher(DcMotorEx m_Launcher, DcMotorEx m_launcherRight){
        launcherMotorLeft = m_Launcher;
        launcherMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcherMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherMotorRight = m_launcherRight;
        launcherMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcherMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherMotorRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setPower(double power){
        launcherMotorLeft.setPower(power);
        launcherMotorRight.setPower(power);
    }

    public void setVelocity(double m_velocity) {
        launcherMotorLeft.setVelocity(m_velocity);
        launcherMotorRight.setVelocity(m_velocity);
    }

    /*public void setRPM(double m_RPM) {
        double m_RPMToVelocity = m_RPM / (6.28 * dblLaunchWheelRadius);
        launcherMotorLeft.setVelocity(m_RPMToVelocity);
    }*/

    public double getVelocity(){
        return launcherMotorLeft.getVelocity();
    }

    /*public double getRPM() {
        return launcherMotorLeft.getVelocity() * (6.28 * dblLaunchWheelRadius);
    }*/

    public void setLaunchVelocity(double m_Distance) {
        double velocity = (0 - ((launchVar1 * m_Distance) + launchVar2));
        setVelocity(velocity);
    }
}
