package org.firstinspires.ftc.teamcode.base;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.subsystems.Camera;
import org.firstinspires.ftc.teamcode.subsystems.ColorSensor;
import org.firstinspires.ftc.teamcode.subsystems.LidarSensor;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class RobotBase {

    public ColorSensor colorSensorSubsystem;
    //public LidarSensor lidarSubsystem;
    //public Camera cameraSubsystem;

    public RobotBase(HardwareMap hwMap){
        colorSensorSubsystem = new ColorSensor(hwMap.get(NormalizedColorSensor.class, "color_sensor"));
        //lidarSubsystem = new LidarSensor(hwMap.get(DistanceSensor.class,"lidar_sensor"));
        //cameraSubsystem = new Camera(hwMap.get(org.firstinspires.ftc.robotcore.external.hardware.camera.Camera.class, "Camera"));

    }
}
