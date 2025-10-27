package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class CameraLight extends SubsystemBase{

    Servo cameraLightServo;

    public CameraLight(Servo m_cameraLightServo){
        cameraLightServo = m_cameraLightServo;
    }

    public Shades enmColorHue;
    public enum Shades {
        OFF(0),
        FULL(1);
        public final double value;
        Shades(double m_color){this.value=m_color; }

    }
    public void setColor(Shades enmTargetColor){
        if (enmTargetColor != enmColorHue ){
            cameraLightServo.setPosition(enmTargetColor.value);
            enmColorHue = enmTargetColor;

        }

    }

}