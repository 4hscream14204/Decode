package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

public class TransferBlocker {
    public Servo transferBlockerServo;

    public enum TransferBlockerPosition{
        BLOCK(0),
        RELEASE(1);
        public final double value;
        TransferBlockerPosition(double pos){
            this.value = pos;
        }
    }

    public TransferBlocker(Servo m_transferBlocker){
        transferBlockerServo = m_transferBlocker;
    }

    public void setPosition(double position){
        transferBlockerServo.setPosition(position);
    }

    public void setPosition(TransferBlockerPosition transferBlockerPosition){
        setPosition(transferBlockerPosition.value);
    }
}
