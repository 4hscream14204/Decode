package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

public class Prism {
    public Servo prism;

    public Prism(Servo m_prism){
        prism = m_prism;
    }

    public void setPosition(double position){
        prism.setPosition(position);
    }

    public void rainbow(){
        setPosition(0.225);
    }
}
