package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DigitalChannel;

public class IntakeDistanceSensor {
    public AnalogInput distanceSensor;
    public IntakeDistanceSensor(AnalogInput m_distanceSensor){
        distanceSensor = m_distanceSensor;
    }

    public double getDistance(){
        return (distanceSensor.getVoltage() / distanceSensor.getMaxVoltage()) * 100;
    }
}
