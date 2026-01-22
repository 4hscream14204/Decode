package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.util.InterpLUT;

public class Launcher extends SubsystemBase {
    public DcMotorEx launcherMotor;
    public final double dblLaunchWheelRadius = 1.375;
    public final double launchVar1 = 2.2787;
    public final double launchVar2 = 1770.4;
    public double dblTargetVel = 0;
    private InterpLUT launcherLUT;
    double maximum = 2300;
    PIDFController launcherPIDF = new PIDFController(0.01,0,0,0.0004);


    public Launcher(DcMotorEx m_Launcher){
        launcherMotor = m_Launcher;
        launcherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcherMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //launcherMotor.setVelocityPIDFCoefficients(9, 0.8, 0, 0.7);
        launcherLUT = new InterpLUT();
        launcherLUT.add(121, 1700);
        launcherLUT.add(131, 1730);
        launcherLUT.add(141, 1750);
        launcherLUT.add(151, 1790);
        launcherLUT.add(161, 1810);
        launcherLUT.add(171, 1840);
        launcherLUT.add(181, 1880);
        launcherLUT.createLUT();
    }

    public void setPower(double power){
        launcherMotor.setPower(power);
    }

    public void setVelocity(double m_velocity) {
        launcherMotor.setPower(launcherPIDF.calculate(getVelocity(), m_velocity));
        dblTargetVel = m_velocity;
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
        if(velocity > maximum){
            setVelocity(maximum);
        }
        else if(velocity < 1700){
            setVelocity(1700);
        }
        else{
        setVelocity(velocity);
        }
    }

    public double getLaunchVelocity(double m_Distance){
        return ((-0.0008*(Math.pow(m_Distance, 2))) + (3.3166 * m_Distance) + 1185.6)/*((0.0071*(Math.pow(m_Distance, 2))) + (0.7714 * m_Distance) + 1503.5)*/;
    }

    public boolean isAtSpeed(double velocity){
        if(Math.abs((getVelocity() - dblTargetVel)) <= 10){
            return true;
        }
        else{
            return false;
        }
    }

    public void setMaximum(double m_maximum){
        maximum = m_maximum;
    }
}
