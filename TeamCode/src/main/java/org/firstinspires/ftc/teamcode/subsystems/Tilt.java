package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Tilt extends SubsystemBase {
    Servo TiltServo;

    public enum Position {
        ACTIVE(0),
        INACTIVE(0);
        public final double value;
        Position(double m_val){this.value = m_val;}
    }
    public Tilt(Servo m_tiltServo) {TiltServo = m_tiltServo;}

    public void setPosition(double position) {TiltServo.setPosition(position);}

}
