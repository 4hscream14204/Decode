package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

import java.util.function.Supplier;

public class BuildPath {
    Follower follower;

    public BuildPath(Follower m_follower){
        follower = m_follower;
    }

    public enum HeadingTypes{
        TANGENTIAL,
        CONSTANT,
        LINEAR
    }

    public enum PathTypes{
        LINE,
        CURVE
    }

    public PathChain buildPath(PathChain m_path, Pose startPointPose, Pose endPointPose){
        m_path = follower.pathBuilder()
                .addPath(new BezierLine(startPointPose, endPointPose))
                .setTangentHeadingInterpolation()
                .build();
        return m_path;
    }

    public PathChain buildPath(PathChain m_path, Pose startPointPose, Pose endPointPose, double heading){
        m_path = follower.pathBuilder()
                .addPath(new BezierLine(startPointPose, endPointPose))
                .setConstantHeadingInterpolation(Math.toRadians(heading))
                .build();
        return m_path;
    }

    public PathChain buildPath(Pose startPointPose, Pose endPointPose, double startHeading, double endHeading){
         return follower.pathBuilder()
                .addPath(new BezierLine(startPointPose, endPointPose))
                .setLinearHeadingInterpolation(Math.toRadians(startHeading), Math.toRadians(endHeading))
                .build();
    }

    public PathChain buildPath(PathChain m_path, Pose startPointPose, Pose controlPointPose, Pose endPointPose, double heading){
        m_path = follower.pathBuilder()
                .addPath(new BezierCurve(startPointPose, controlPointPose, endPointPose))
                .setConstantHeadingInterpolation(Math.toRadians(heading))
                .build();
        return m_path;
    }

    public PathChain buildPath(PathChain m_path, Pose startPointPose, Pose controlPointPose, Pose endPointPose){
        m_path = follower.pathBuilder()
                .addPath(new BezierCurve(startPointPose, controlPointPose, endPointPose))
                .setTangentHeadingInterpolation()
                .build();
        return m_path;
    }

    public PathChain buildPath(PathChain m_path, Pose startPointPose, Pose controlPointPose, Pose endPointPose, double startHeading, double endHeading){
        m_path = follower.pathBuilder()
                .addPath(new BezierCurve(startPointPose, controlPointPose, endPointPose))
                .setLinearHeadingInterpolation(Math.toRadians(startHeading), Math.toRadians(endHeading))
                .build();
        return m_path;
    }

    public Supplier<PathChain> buildPath(Supplier<PathChain> m_path, Pose startPointPose, Pose endPointPose, double endHeading){
        m_path =()-> follower.pathBuilder()
                .addPath(new Path(new BezierCurve(startPointPose, endPointPose)))
                .setLinearHeadingInterpolation(follower.getHeading(), Math.toRadians(endHeading))
                .build();
        return m_path;
    }
}
