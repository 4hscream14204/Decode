package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Tilt extends SubsystemBase {
    Servo TiltServoL;
    Servo tiltServoR;
    public Position tiltPositionL;
    public Position tiltPositionR;

    public enum Position {
        LEFTACTIVE(0.333),
        LEFTINACTIVE(1),
        RIGHTACTIVE(0.666),
        RIGHTINACTIVE(0);
        public final double value;
        Position(double m_val){this.value = m_val;}
    }
    public Tilt(Servo m_tiltServoL, Servo m_tiltServoR) {
        TiltServoL = m_tiltServoL;
        tiltServoR = m_tiltServoR;
    }

    public void setPosition(Position positionL, Position positionR) {
        tiltPositionL = positionL;
        tiltPositionR = positionR;
        TiltServoL.setPosition(positionL.value);
        tiltServoR.setPosition(positionR.value);
    }

    public Position getPositionL(){
        return tiltPositionL;
    }

    public Position getPositionR(){
        return tiltPositionR;
    }

}
