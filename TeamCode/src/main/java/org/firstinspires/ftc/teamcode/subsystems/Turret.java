package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

public class Turret {
    Servo turretServoL;
    Servo turretServoR;
    double turretServoPosition;
    public Turret(Servo m_turretServoL, Servo m_turretServoR){
        turretServoL = m_turretServoL;
        turretServoR = m_turretServoR;
    }

    public double convertDegToServoPos(double degree){
        return (degree / 360);
    }

    public void setPosition(double positionDeg){
        if(convertDegToServoPos(positionDeg) > 270 && convertDegToServoPos(positionDeg) < 300){
            turretServoL.setPosition(convertDegToServoPos(270));
            turretServoR.setPosition(convertDegToServoPos(270));
        }
        else{
            turretServoL.setPosition(convertDegToServoPos(positionDeg));
            turretServoR.setPosition(convertDegToServoPos(positionDeg));
            turretServoPosition = convertDegToServoPos(positionDeg);
        }
    }
}
