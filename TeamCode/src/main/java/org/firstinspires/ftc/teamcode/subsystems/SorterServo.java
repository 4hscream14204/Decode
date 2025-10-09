package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class SorterServo extends SubsystemBase {
    Servo sorterServo;
    public enum ServoPosition {
        EJECT(1),
        STABLE(0);
        public final int position;

        ServoPosition(int high) {
            this.position = high;
        }

    }
    public ServoPosition enmServoPosition;
    public SorterServo(Servo m_Servo) {
        sorterServo = m_Servo;
    }

    public void setPosition(ServoPosition m_ServoPosition){
        sorterServo.setPosition(m_ServoPosition.position);
    }
}
