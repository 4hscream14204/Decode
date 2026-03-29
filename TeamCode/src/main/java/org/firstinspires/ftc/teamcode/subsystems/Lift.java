package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.controller.PIDFController;

public class Lift {
    public Servo liftServoL;
    public Servo liftServoR;
    public PIDFController liftPIDF = new PIDFController(0.05, 0, 0, 0);

    public Lift(Servo m_liftServoL){
        liftServoL = m_liftServoL;
        //liftServoR = m_liftServoR;
    }

    public void setPosition(double position){
        liftServoL.setPosition(position);
        //liftServoR.setPosition(position);
    }

    public void extend(){
        setPosition(1);
    }

    public void retract(){
        setPosition(0);
    }

    public void stop(){
        setPosition(0.5);
    }
}
