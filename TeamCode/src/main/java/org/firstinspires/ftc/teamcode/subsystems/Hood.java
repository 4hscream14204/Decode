package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.SimpleServo;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

public class Hood extends SubsystemBase {
    ServoEx hoodServo;
    double rangeOfMotion = 300;
    double minAngle = 0;
    double maxAngle = 180;
    double position;

    public Hood(ServoEx m_hoodServo, HardwareMap hwMap){
        hoodServo = m_hoodServo;
        hoodServo = new ServoEx(hwMap, "hoodServo", 90, 180);
    }

    public void setPosition(double m_position){
        hoodServo.set(m_position);
    }

    public void autoSetPosition(double targetAngle){
        position = (targetAngle - minAngle) / (maxAngle - minAngle);
        setPosition(position);
    }
}
