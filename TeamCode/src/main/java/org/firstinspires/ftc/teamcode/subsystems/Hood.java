package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

public class Hood {
    public Servo hoodServo;

    public enum HoodPosition{
        CLOSE(0),
        FAR(1);
        public final double value;
        HoodPosition(double pos){
            this.value = pos;
        }
    }

    public Hood (Servo m_hoodServo){
        hoodServo = m_hoodServo;
    }

    public void setPosition(double position){
        hoodServo.setPosition(position);
    }

    public void setPosition(HoodPosition position){
        setPosition(position.value);
    }
}
