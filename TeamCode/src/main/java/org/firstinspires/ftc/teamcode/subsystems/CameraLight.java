package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class CameraLight extends SubsystemBase{

    Servo cameraLightServo;

    public CameraLight(Servo m_cameraLightServo){
        cameraLightServo = m_cameraLightServo;
        setShade(Shades.HALF);
    }

    public Shades enmColorHue;
    public enum Shades {
        OFF(0),
        FIFTH(0.2),
        TESTRIGHT(0.4),
        HALF(0.5),
        FULL(1);
        public final double value;
        Shades(double m_color){this.value=m_color; }

    }
    public void setShade(Shades enmTargetColor){
        if (enmTargetColor != enmColorHue ){
            cameraLightServo.setPosition(enmTargetColor.value);
            enmColorHue = enmTargetColor;

        }

    }

}
