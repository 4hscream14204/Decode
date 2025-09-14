package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Limelight Heading Test")
public class LimelightConnorHeadingTest extends OpMode {

    Limelight3A limelight3A;
    LLResult result;

    @Override
    public void init(){
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        result = limelight3A.getLatestResult();
        limelight3A.pipelineSwitch(2);
    }

    @Override
    public void loop(){
        result.getPipelineIndex();
        telemetry.addData("AprilTag angle", result.getBotpose());
    }
}
