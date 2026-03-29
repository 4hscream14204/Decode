package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.teamcode.base.RobotBase;

public class LiftDistanceSensor {

    public enum LiftPosition{
        PARKMM(444.5),
        INACTIVE(0);
        public final double position;
        LiftPosition(double value){
            this.position = value;
        }
    }

    AnalogInput liftDistanceSensor;
    double distance;
    double maxDistance = 1000;
    double maxVolts = 3.3;
    public LiftDistanceSensor(AnalogInput m_liftDistanceSensor){
        liftDistanceSensor = m_liftDistanceSensor;
    }

    public double getPosition(){
        distance = (liftDistanceSensor.getVoltage() / maxVolts) * maxDistance;
        return distance;
    }
}
