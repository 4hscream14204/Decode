package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

public class Limelight {
    public Limelight3A limelight;
    LLResult result;
    double limelightTX;
    double limelightTY;
    double limelightDistance;
    double limelightHeight = 15.83;
    double mountingAngle = 0;

    public Limelight(Limelight3A m_limelight, int m_pipeline){
        limelight = m_limelight;
        limelight.pipelineSwitch(m_pipeline);
        limelight.setPollRateHz(100);
        limelight.start();
    }

    public void updateLimelight(){
        result = limelight.getLatestResult();

        limelightTX = result.getTx();
        limelightTY = result.getTy();
        limelightDistance = ((limelightHeight) / (Math.tan(Math.toRadians(mountingAngle - limelightTY))));
    }

    public double getDistance(){
        return limelightDistance;
    }
}
