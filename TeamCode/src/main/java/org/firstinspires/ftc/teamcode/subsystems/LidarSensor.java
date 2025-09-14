package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class LidarSensor extends SubsystemBase {

    public DistanceSensor lidarSensor;

    public LidarSensor (DistanceSensor m_lidar) {
        lidarSensor = m_lidar;
    }

    public double getDistance(){
        return lidarSensor.getDistance(DistanceUnit.CM);
    }
}
