package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.hardware.sparkfun.SparkFunLEDStick;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "SparkfunLightStickTest")
public class SparkfunLightStickTest extends OpMode {

    SparkFunLEDStick ledStick;

    @Override
    public void init() {
        ledStick = hardwareMap.get(SparkFunLEDStick.class, "ledStick");
    }

    @Override
    public void loop() {
        ledStick.setColor(0, 5);
    }
}
