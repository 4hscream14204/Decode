package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class RGBLightSubsystem extends SubsystemBase{

    Servo RGBLightServo;

    public RGBLightSubsystem (Servo RGBLightServo){
        RGBLightServo = RGBLightServo;
    }

public Colors enmColorHue;
    public enum Colors{
        purple(.7183),
        green(.4944);
        public final double value;
        Colors(double m_color){this.value=m_color; }

    }
    public void setColor(Colors enmTargetColor){
        if (enmTargetColor != enmColorHue ){
            RGBLightServo.setPosition(enmTargetColor.value);
            enmColorHue = enmTargetColor;

        }

    }

        }



