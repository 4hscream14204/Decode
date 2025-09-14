package org.firstinspires.ftc.teamcode.subsystems;

import android.graphics.Color;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class ColorSensor extends SubsystemBase {

    final float[] hsvValues = new float[3];
    float gain = 3;

    public NormalizedColorSensor colorSensor;

    public ColorSensor(NormalizedColorSensor m_colorSensor) {
        colorSensor = m_colorSensor;
        colorSensor.setGain(gain);

    }

    public double getHueValues() {

        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsvValues);
        return hsvValues[0];
    }
}
