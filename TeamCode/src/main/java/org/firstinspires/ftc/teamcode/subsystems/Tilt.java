package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Tilt extends SubsystemBase {
    Servo tiltServo;
    public Tilt(Servo m_tiltServo) {tiltServo = m_tiltServo;}

    public void setPosition(double position) {tiltServo.setPosition(position);}

}
