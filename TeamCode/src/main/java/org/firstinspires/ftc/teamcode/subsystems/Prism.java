package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;

public class Prism {

    public enum PrismModes{
        RAINBOW(0.255),
        LAUNCH(0.898),
        PARK(0.913),
        RED(0.069),
        BLUE(0.838);
        public final double value;
        PrismModes(double val){this.value = val;}
    }

    public Servo prism;

    public Prism(Servo m_prism){
        prism = m_prism;
    }

    public void setPosition(double position){
        prism.setPosition(position);
    }

    public void setPosition(PrismModes m_mode){
            prism.setPosition(m_mode.value);
    }

    public void setAllianceColor(){
        if (DataStorage.alliance == DecodeEnums.Alliance.RED) {
            setPosition(PrismModes.RED);
        } else {
            setPosition(PrismModes.BLUE);
        }
    }

    public void rainbow(){
        setPosition(0.225);
    }
}
