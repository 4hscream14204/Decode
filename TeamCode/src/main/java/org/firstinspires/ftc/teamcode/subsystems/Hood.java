package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class Hood extends SubsystemBase {
    Servo hoodServo;
    public Hood(Servo m_hoodServo){
        hoodServo = m_hoodServo;
    }

    public void setPosition(double position){
        hoodServo.setPosition(position);
    }
}
