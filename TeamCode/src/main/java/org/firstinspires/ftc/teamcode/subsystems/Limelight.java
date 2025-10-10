package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

public class Limelight extends SubsystemBase {

    public enum limelightPiplines {
        OBELISK(2),
        GREENARTIFACT(1),
        REDGOAL(4),
        BLUEGOAL(3),
        PURPLEARTIFACT(0);
        public final int value;
        limelightPiplines(int m_val){
            this.value = m_val;
        }
    }

    public Limelight3A limelight;
    public double limelightTX;
    public double limelightTY;
    public String limelightPiplineType;
    public double limelightTa;
    public double mountingAngle = 0;
    public final double goalAprilTagHeight = 70.27888/*72.69431*//*67.86345*//*63.0497917*//*74.75*/;
    public final double goalHeight = 99;
    public double limelightHeight = 40.2;
    public double goalHeightOffset = goalHeight - limelightHeight;
    public final double gravity = 981;
    public double shooterOffsetY = 17;
    public double ShooterOffsetX = 0;
    public double x;
    public Pose3D botPose;

    public Limelight.limelightPiplines enmLimelightPiplines;

    public Limelight(Limelight3A m_limelight) {
        limelight = m_limelight;
    }

    public void initLimelight() {

        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.pipelineSwitch(0);
        limelight.start();



        LLResult result = limelight.getLatestResult();
        List<LLResultTypes.ColorResult> fiducials = result.getColorResults();
        int id = 0;
        for(LLResultTypes.ColorResult fiducial :fiducials) {
            double limelightx = fiducial.getTargetXDegrees(); // Where it is (left-right)
            double limelighty = fiducial.getTargetYDegrees(); // Where it is (up-down)
            double StrafeDistance_3D = fiducial.getRobotPoseTargetSpace().getPosition().y;
            botPose = fiducial.getRobotPoseFieldSpace();


            //telemetry.addData("Fiducial " + id, "is " + StrafeDistance_3D + " meters away");
        }
    }

    public double getAngleToGoal(){
        return mountingAngle + getTargetY();
    }

    public double getHorizontalDistance(double m_Offset){
        return (((goalAprilTagHeight - limelightHeight) / Math.tan(Math.toRadians(getAngleToGoal()))) /*+ m_Offset*/);
    }

    public double getVerticalDistance(double m_Offset){
        return (goalHeightOffset) + m_Offset;
    }

    /*public double getHorizontalComp(){
        return getHorizontalDistance(ShooterOffsetX) / getArcTime();
    } */

    public double getVerticalComp(){
        return ((getVerticalDistance(shooterOffsetY) * 2) / getArcTime());
    }

    public double getArcTime(){
        return Math.pow((getVerticalDistance(shooterOffsetY) / gravity), 0.5) /* (.5 * (getVerticalDistance(shooterOffsetY) / gravity))*/;
    }

    public double getLaunchSpeed(){
        return (((gravity * getHorizontalDistance(ShooterOffsetX)) * (gravity * getHorizontalDistance(ShooterOffsetX))) / (2 * getVerticalDistance(shooterOffsetY))) + (2 * getVerticalDistance(shooterOffsetY) * gravity) * (.5 * (((gravity * getHorizontalDistance(ShooterOffsetX)) * (gravity * getHorizontalDistance(ShooterOffsetX))) / (2 * getVerticalDistance(shooterOffsetY))) + (2 * getVerticalDistance(shooterOffsetY) * gravity));
    }

    public double getLaunchAngle() {
        return 1/*Math.asin(getVerticalComp() / getLaunchSpeed())*/;
    }

    /*public double getLaunchRPM() {
        return ;
    }*/

    public void updateLimelight(){
        LLResult result = limelight.getLatestResult();
        limelightTX = result.getTx();
        limelightTY = result.getTy();
        limelightPiplineType = result.getPipelineType();
        limelightTa = result.getTa();
    }

    public void changePipline(Limelight.limelightPiplines m_pipline){
        enmLimelightPiplines = m_pipline;
        limelight.pipelineSwitch(m_pipline.value);
    }

    public double getTargetX(){
        return limelightTX;
    }

    public double getTargetY() {
        return limelightTY;
    }

    public String getPipline() {
        return limelightPiplineType;
    }

    public double getTargetArea() {
        return limelightTa;
    }
}
