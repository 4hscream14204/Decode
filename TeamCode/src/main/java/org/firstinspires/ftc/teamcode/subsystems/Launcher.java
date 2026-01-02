package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.util.InterpLUT;

public class Launcher extends SubsystemBase {
    public DcMotorEx launcherMotor;
    public final double dblLaunchWheelRadius = 1.375;
    public final double launchVar1 = 2.2787;
    public final double launchVar2 = 1770.4;
    private InterpLUT launcherLUT;

    public Launcher(DcMotorEx m_Launcher){
        launcherMotor = m_Launcher;
        launcherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherMotor.setVelocityPIDFCoefficients(9, 0.8, 0, 0.7);
        launcherLUT = new InterpLUT();
        launcherLUT.add(128, 1730);
        launcherLUT.add(168, 1770);
        launcherLUT.add(188, 1780);
        launcherLUT.add(208, 1800);
        launcherLUT.add(248, 1820);
        launcherLUT.add(248, 1820);
        launcherLUT.add(280, 1940);
        launcherLUT.add(326, 2090);
        launcherLUT.createLUT();
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
        return launcherLUT.get(m_Distance);
    }

    public boolean isAtSpeed(double velocity){
        if(Math.abs((getVelocity() - velocity)) == 20){
            return true;
        }
        else{
            return false;
        }
    }
}
