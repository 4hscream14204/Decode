package org.firstinspires.ftc.teamcode.base;

import com.pedropathing.geometry.Pose;

public class DataStorage {
    public static DecodeEnums.Patterns pattern = DecodeEnums.Patterns.PPG;
    public static  DecodeEnums.Alliance alliance = DecodeEnums.Alliance.RED;
    public static Pose endPosition = new Pose(94, 10, Math.toRadians(90));
    public static Pose redGoalPose = new Pose(144, 148);
    public static Pose blueGoalPose = new Pose(0, 140);
}
