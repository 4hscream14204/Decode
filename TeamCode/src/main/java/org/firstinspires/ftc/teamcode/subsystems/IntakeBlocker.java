package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

public class IntakeBlocker {
        Servo IntakeBlockerServo;
        public Position blockerPosition;

    public enum Position {
        OPEN(0.755),
        CLOSED(1);
        public final double value;
        Position(double m_val){this.value = m_val;}
    }

        public IntakeBlocker(Servo m_intakeBlockerServo) {IntakeBlockerServo = m_intakeBlockerServo;}

        public void setPosition(Position position) {
            blockerPosition = position;
            IntakeBlockerServo.setPosition(position.value);
        }

        public Position getPosition(){
            return blockerPosition;
        }

    }

