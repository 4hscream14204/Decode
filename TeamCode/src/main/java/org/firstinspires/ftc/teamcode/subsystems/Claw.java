package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo srvClaw;
    private boolean bolClawOpen;
    private double dblOpenPos = 0.28611;
    private double dblClosedPos = 0.3222;

    public Claw(Servo m_srvClaw) {
        srvClaw = m_srvClaw;
        openClaw();
    }

    public void openClaw() {
        bolClawOpen = true;
        srvClaw.setPosition(dblOpenPos);
    }

    public void closeClaw() {
        bolClawOpen = false;
        srvClaw.setPosition(dblClosedPos);
    }

    public boolean isClawOpen() {
        if (bolClawOpen) {
            return true;
        } else {
            return false;
        }
    }

    public void toggleClaw() {
        if (isClawOpen()) {
            closeClaw();
        } else {
            openClaw();
        }
    }
}