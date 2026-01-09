package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class SorterServo extends SubsystemBase {
    public Servo sorterServo;
    public enum ServoPosition {
        LAUNCH(0.88),
        HOME(1),
        PRELOAD(0.1388);
        public final double position;

        ServoPosition(double high) {
            this.position = high;
        }

    }
    public ServoPosition enmServoPosition;
    public SorterServo(Servo m_Servo) {
        sorterServo = m_Servo;
    }

    public void setPosition(ServoPosition m_ServoPosition){
        sorterServo.setPosition(m_ServoPosition.position);
        enmServoPosition = m_ServoPosition;
    }

    public ServoPosition getPosition(){
        return enmServoPosition;
    }
}
