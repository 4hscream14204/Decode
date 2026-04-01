package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

public class IntakePivot {
    public Servo intakePivotServo;

    public enum PivotPosition{
        BLOCK(0),
        INTAKE(1);
        public final double value;

        PivotPosition(double pos){
            this.value = pos;
        }
    }

    public IntakePivot(Servo m_intakePivotServo){
        intakePivotServo = m_intakePivotServo;
    }

    public void setPosition(double position){
        intakePivotServo.setPosition(position);
    }

    public void setPosition(PivotPosition position){
        intakePivotServo.setPosition(position.value);
    }
}
