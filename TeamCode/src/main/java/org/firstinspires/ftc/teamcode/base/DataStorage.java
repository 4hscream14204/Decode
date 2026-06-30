package org.firstinspires.ftc.teamcode.base;

import com.pedropathing.geometry.Pose;

public class DataStorage {
    public static DecodeEnums.Patterns pattern = DecodeEnums.Patterns.PPG;
    public static  DecodeEnums.Alliance alliance = DecodeEnums.Alliance.RED;
    public static Pose endPosition = new Pose(108, 10, Math.toRadians(90));
    public static DecodeEnums.LaunchingMode launchingMode = DecodeEnums.LaunchingMode.GOAL;
    public static Pose redGoalPose = new Pose(188, 200);
    public static Pose blueGoalPose = new Pose(-24, 200);
    public static Pose launcherRedGoalPose = new Pose(178, 170);
    public static Pose launcherBlueGoalPose = new Pose(168, 160).mirror();
    public static Pose bluePrismPose = new Pose(82, 200);
    public static Pose redPrismPose = new Pose(82, 200).mirror();
    public static Pose launcherRedPrismPose = new Pose(98, 192);
    public static Pose launcherBluePrismPose = new Pose(92, 192);
    //82, 182
}
