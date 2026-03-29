package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DigitalChannel;

public class IntakeDistanceSensor {
    public DigitalChannel distanceSensor;
    public IntakeDistanceSensor(DigitalChannel m_distanceSensor){
        distanceSensor = m_distanceSensor;
        distanceSensor.setMode(DigitalChannel.Mode.INPUT);
    }

    public boolean getState(){
        return distanceSensor.getState();
    }
}
