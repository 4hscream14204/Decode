package org.firstinspires.ftc.teamcode.pidtopoint;

import com.seattlesolvers.solverslib.controller.PIDFController;

public class ChassisConstants {
    public static PIDFController xPID = new PIDFController(0, 0, 0, 0);

    public static PIDFController yPID = new PIDFController(0, 0, 0, 0);

    public static PIDFController headingPID = new PIDFController(0, 0, 0, 0);
}
