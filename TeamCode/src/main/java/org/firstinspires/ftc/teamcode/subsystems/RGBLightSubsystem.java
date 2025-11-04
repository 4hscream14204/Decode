package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class RGBLightSubsystem extends SubsystemBase{

    Servo RGBLightServo;


    public RGBLightSubsystem (Servo m_RGBLightServo){
        RGBLightServo = m_RGBLightServo;
    }

public RGBLightSubsystem.Colors enmColorHue;
    public enum Colors{
        BLUE(0.611),
        RED(0.2877777),
        PURPLE(.7183),
        GREEN(.4944);
        public final double value;
        Colors(double m_color){this.value=m_color; }

    }
    public void setColor(RGBLightSubsystem.Colors enmTargetColor){
        if (enmTargetColor != enmColorHue ){
            RGBLightServo.setPosition(enmTargetColor.value);
            enmColorHue = enmTargetColor;

        }

    }

        }



