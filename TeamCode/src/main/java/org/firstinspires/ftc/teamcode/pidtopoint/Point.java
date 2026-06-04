package org.firstinspires.ftc.teamcode.pidtopoint;

import com.pedropathing.geometry.Pose;

public class Point {
    private double x;
    private double y;
    private double heading;

    public enum AngleMode{
        DEGREES,
        RADIANS
    }

    public Point(double m_x, double m_y){
        x = m_x;
        y = m_y;
    }

    public Point(double m_x, double m_y, double m_headingRad){
        x = m_x;
        y = m_y;
        heading = m_headingRad;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getHeadingRad(){
        return heading;
    }

    public double getHeadingDeg(){
        return Math.toDegrees(heading);
    }

    public double getHeading(AngleMode m_angleMode){
        if(m_angleMode == AngleMode.DEGREES){
            return getHeadingDeg();
        }
        else{
            return getHeadingRad();
        }
    }

    public Point poseToPoint(Pose m_pose){
        return new Point(m_pose.getX(), m_pose.getY(), m_pose.getHeading());
    }
}
