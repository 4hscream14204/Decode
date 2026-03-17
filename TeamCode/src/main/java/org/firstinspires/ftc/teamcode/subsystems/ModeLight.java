package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotbase.prism.GoBildaPrismDriver;

public class ModeLight {

    GoBildaPrismDriver prismDriver;

    public I2cDevice modeLight;

    int parkTime = 110;

    public ModeLight(GoBildaPrismDriver m_modeLight) {
        prismDriver = m_modeLight;
    }

    public void updateIndicatorColor(ElapsedTime m_time, boolean m_locked) {

        boolean timeToPark = m_time.seconds() > parkTime;

        if(timeToPark && m_locked) {
            prismDriver.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
        } else if(timeToPark && !m_locked) {
            prismDriver.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_1);
        } else if(!timeToPark && m_locked) {
            prismDriver.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_2);
        } else {
            prismDriver.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_3);
        }
    }
}
