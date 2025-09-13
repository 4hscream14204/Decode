package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw extends SubsystemBase {
    private Servo srvClaw;
    private boolean bolClawOpen;
    private double dblOpenPos = 0;
    private  double dblClosedPos = 0.35;

    public Claw(Servo m_srvClaw) {
        srvClaw = m_srvClaw;
        openClaw();
    }

     public void closeClaw(){
        bolClawOpen = true;
        srvClaw.setPosition(dblOpenPos);
     }
     public void openClaw(){
        bolClawOpen = false;
        srvClaw.setPosition(dblClosedPos);
     }

     public boolean isClawOpen(){
        if (bolClawOpen){
            return true;
        } else {
            return false;
        }
     }

     public void toggleClaw(){
        if (isClawOpen()){
            closeClaw();
        } else {
            openClaw();
        }
     }
}
