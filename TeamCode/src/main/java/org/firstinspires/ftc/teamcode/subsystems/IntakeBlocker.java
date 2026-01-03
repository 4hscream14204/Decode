package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class IntakeBlocker {
        Servo IntakeBlockerServo;
        public IntakeBlocker(Servo m_intakeBlockerServo) {IntakeBlockerServo = m_intakeBlockerServo;}

        public void setPosition(double position) {
            IntakeBlockerServo.setPosition(position);}

    }

