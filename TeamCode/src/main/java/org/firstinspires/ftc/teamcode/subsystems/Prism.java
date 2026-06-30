package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;

public class Prism {

    public enum PrismModes{
        NONE(0, 0),
        RAINBOW(0.255, 1),
        LAUNCH(0.898, 4),
        PARK(0.913, 3),
        RED(0.069, 2),
        BLUE(0.838, 2),
        RAINBOWBLUE(0.20222, 2),
        RAINBOWRED(0.21778, 2);
        public final double value;
        public final int priority;
        PrismModes(double val, int pri){this.value = val; this.priority = pri;}
    }

    public PrismModes prismModes = PrismModes.NONE;

    public Servo prism;

    public Prism(Servo m_prism){
        prism = m_prism;
    }

    public void setPosition(double position){
        prism.setPosition(position);
    }

    public void setPosition(PrismModes m_mode){
        prismModes = m_mode;
        prism.setPosition(m_mode.value);
    }

    public void setAllianceColor(DecodeEnums.LaunchingMode m_launchMode){
        if (DataStorage.alliance == DecodeEnums.Alliance.RED) {
            if(m_launchMode == DecodeEnums.LaunchingMode.GOAL) {
                setMode(PrismModes.RED, false);
            } else {
                setMode(PrismModes.RAINBOWRED, false);
            }
        } else {
            if(m_launchMode == DecodeEnums.LaunchingMode.GOAL) {
                setMode(PrismModes.BLUE, false);
            } else {
                setMode(PrismModes.RAINBOWBLUE, false);
            }
        }
    }

    public void setGamePhase(ElapsedTime time){
        if(time.seconds() > 110){
            setMode(PrismModes.PARK, false);
        } else {
            setMode(PrismModes.RAINBOW, false);
        }
    }

    public void rainbow(){
        setPosition(0.225);
    }

    public PrismModes getMode(){
        return prismModes;
    }

    public void setMode(PrismModes m_mode, boolean m_overwrite){

        if(m_mode.priority <= prismModes.priority && !m_overwrite){
            return;
        }

        setPosition(m_mode);
    }
}
