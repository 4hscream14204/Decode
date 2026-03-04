package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class Hood extends SubsystemBase {
    Servo hoodServoL;
    Servo hoodServoR;
    double rangeOfMotion = 300;

    public enum HoodPosition{
        MAX(0.33),
        FAR(0.555),
        CLOSE(0.72),
        MIN(1);
        public final double value;
        HoodPosition(double m_val){
            this.value = m_val;
        }
    }

    public Hood(Servo m_hoodServo, Servo m_hoodServoR){
        hoodServoL = m_hoodServo;
        hoodServoR = m_hoodServoR;
    }

    public void setPosition(double position){
        hoodServoL.setPosition(position);
        hoodServoR.setPosition(position);
    }

    public void setPosition(HoodPosition position){
        hoodServoL.setPosition(position.value);
        hoodServoR.setPosition(position.value);
    }

    public void autoSetPosition(double angle){
        hoodServoL.setPosition(angle/rangeOfMotion);
        hoodServoR.setPosition(angle/rangeOfMotion);
    }
}
