package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class SorterServo extends SubsystemBase {
    public Servo sorterServo;
    public enum ServoPosition {
        TRANSFER(0.222),
        STABLE(0.011),
        PRELOAD(0.1388),
        TEST1(0),
        TEST2(0.5),
        TEST3(1);
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
    }
}
