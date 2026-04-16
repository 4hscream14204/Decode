package org.firstinspires.ftc.teamcode.base;

import com.qualcomm.robotcore.hardware.Servo;

public class SCREAMServo{
    Servo servo;
    public SCREAMServo(Servo m_Servo){
        servo = m_Servo;
    }

    public void setPositionPWM(double pwmPosition){
        setPosition((((pwmPosition - 600) / 1800)));
    }

    public void setPosition(double position){
        servo.setPosition(position);
    }
}
