package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class Camera extends SubsystemBase {

    public org.firstinspires.ftc.robotcore.external.hardware.camera.Camera camera;

    public Camera(org.firstinspires.ftc.robotcore.external.hardware.camera.Camera m_camera) {
        camera = m_camera;


    }
}
