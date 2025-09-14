package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm extends SubsystemBase {
    public enum ArmPosition {
        HOME(0),
        GROUND(0.03),
        LOW(0.125),
        MEDIUM(0.19),
        HIGH(0.27),;
        public final double value;
        ArmPosition(double m_position){
            this.value = m_position;
        }
    }

Servo srvArm;
    ArmPosition enumArmPosition;

    public Arm(Servo m_srvArm) {
        srvArm = m_srvArm;
        goToPosition(ArmPosition.HOME);
    }

    public void goToPosition(ArmPosition m_enumArmPosition) {
        enumArmPosition = m_enumArmPosition;
        srvArm.setPosition(enumArmPosition.value);
    }

    public ArmPosition getPosition() {
        return enumArmPosition;
    }

}
