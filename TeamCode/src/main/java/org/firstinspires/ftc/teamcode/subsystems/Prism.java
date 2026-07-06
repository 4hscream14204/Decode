package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;

public class Prism {

    public enum PrismModes{
        NONE(0, 0),
        RAINBOW(0.2261, 1),
        LAUNCH(0.898, 4),
        PARK(0.913, 1),
        RED(0.057, 2),
        BLUE(0.838, 2),
        RAINBOWBLUE(0.20222, 2),
        RAINBOWRED(0.21778, 2),
        ALLIANCE(0, 2),
        GAMEPHASE(0, 3);
        public final double value;
        public final int priority;
        PrismModes(double val, int pri){this.value = val; this.priority = pri;}
    }

    public PrismModes prismModes = PrismModes.NONE;

    public Servo prism;

    public Prism(Servo m_prism){
        prism = m_prism;
    }

    public void setPosition(PrismModes m_mode){
        prism.setPosition(m_mode.value);
    }

    public void setAllianceColor(){
        if (DataStorage.alliance == DecodeEnums.Alliance.RED) {
            if(DataStorage.launchingMode == DecodeEnums.LaunchingMode.GOAL) {
                setPosition(PrismModes.RED);
            } else {
                setPosition(PrismModes.RAINBOWRED);
            }
        } else {
            if(DataStorage.launchingMode == DecodeEnums.LaunchingMode.GOAL) {
                setPosition(PrismModes.BLUE);
            } else {
                setPosition(PrismModes.RAINBOWBLUE);
            }
        }
    }

    public void setGamePhase(ElapsedTime time){
        if(time.seconds() > 110){
            setPosition(PrismModes.PARK);
        } else {
            setPosition(PrismModes.RAINBOW);
        }
    }

    public PrismModes getMode(){
        return prismModes;
    }

    public void setMode(PrismModes m_mode, boolean m_overwrite){

        if((m_mode.priority <= prismModes.priority && !m_overwrite) || m_mode == prismModes){
            return;
        }

        prismModes = m_mode;
    }

    public void updateLights(ElapsedTime m_gameTime){
        if(prismModes == PrismModes.ALLIANCE) {
            setAllianceColor();
        } else if (prismModes == PrismModes.GAMEPHASE) {
            setGamePhase(m_gameTime);
        } else {
            setPosition(prismModes);
        }
    }
}