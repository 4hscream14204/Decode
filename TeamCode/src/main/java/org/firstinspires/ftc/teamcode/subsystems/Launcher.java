package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.controller.PIDFController;

import java.util.ArrayList;
import java.util.List;

public class Launcher{

    public final double dblLaunchWheelRadius = 1.375;
    public final double launchVar1 = 2.2787;
    public final double launchVar2 = 1770.4;
    public DcMotorEx launcherMotor;
    public double dblTargetVel = 0;
    double maximum = 2500;
    double changeThresholdPower = 0.001;
    double changeThresholdVelocity = 3;
    int velStorageSize = 3;
    PIDFController launcherPIDF = new PIDFController(0.01,0,0,0.0004);
    List<Double> velStorage = new ArrayList<>();

    public Launcher(DcMotorEx m_Launcher){
    launcherMotor = m_Launcher;
    launcherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    launcherMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    //launcherMotor.setVelocityPIDFCoefficients(9, 0.8, 0, 0.7);

    for (int i = 0; i < velStorageSize; i++) {
        velStorage.add(0.0);
    }
}

public void setPower(double power){
    if(Math.abs(power - launcherMotor.getPower()) > changeThresholdPower){
        launcherMotor.setPower(power);
    }
}

public void setVelocity(double m_velocity) {
    launcherMotor.setPower(launcherPIDF.calculate(getVelocity(), m_velocity));
    velStorage.add(getVelocity());
    velStorage.remove(0);
    dblTargetVel = m_velocity;
}

public void setVelocitySimple(double m_velocity) {
    if(Math.abs(m_velocity - launcherMotor.getVelocity()) > changeThresholdVelocity) {
        launcherMotor.setVelocity(m_velocity);
    }
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
    //return ((3.1834 * m_Distance) + 1240.5);
    return ((0.005*(Math.pow(m_Distance, 2))) + (0.5651 * m_Distance) + 1618.2)/*((0.0071*(Math.pow(m_Distance, 2))) + (0.7714 * m_Distance) + 1503.5)*/;
}

public boolean isAtSpeed(double velocity){
    double averageSpeed = 0;
    int velStorageIndex = 0;

    for (int i = 0; i < velStorageSize; i++) {
        averageSpeed = averageSpeed + velStorage.get(velStorageIndex);
        velStorageIndex ++;
    }

    averageSpeed = averageSpeed / velStorageSize;

    return Math.abs((averageSpeed - dblTargetVel)) <= 10;
}

public void setMaximum(double m_maximum){
    maximum = m_maximum;
}
}
