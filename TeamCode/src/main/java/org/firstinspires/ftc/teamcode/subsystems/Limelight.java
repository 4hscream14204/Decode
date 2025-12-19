package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

public class Limelight extends SubsystemBase {

    public enum limelightPipelines {
        OBELISK(2),
        GREENARTIFACT(1),
        REDGOAL(4),
        BLUEGOAL(3),
        PURPLEARTIFACT(0);
        public final int value;
        limelightPipelines(int m_val){
            this.value = m_val;
        }
    }

    public Limelight3A limelight;
    public double limelightTX;
    public double limelightTY;
    public String limelightPiplineType;
    public double limelightTa;
    public double limeLightID;
    public double mountingAngle = 0;
    public final double goalAprilTagHeight = 74.75;
    public final double goalHeight = 99;
    public double limelightHeight = 40.2;
    public double goalHeightOffset = goalHeight - limelightHeight;
    public final double gravity = 981;
    public double shooterOffsetY = 0;
    public double shooterOffsetX = 0;
    public double yClearance = 17;
    public double x;
    public double y;
    public double distance;
    public double id;
    public double limelightTZ;
    public Pose3D botPose;

    LLResult result;

    public limelightPipelines enmLimelightPipelines;

    public Limelight(Limelight3A m_limelight) {
        limelight = m_limelight;
    }

    public void initLimelight(limelightPipelines m_pipeline) {

        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.pipelineSwitch(m_pipeline.value);
        limelight.start();


        result = limelight.getLatestResult();
        List<LLResultTypes.FiducialResult> aprilTagResult = result.getFiducialResults();
        //loop through the list, even if there is only 1 item since it is a List type
        for (LLResultTypes.FiducialResult fr : aprilTagResult) {
            id = fr.getFiducialId();
            x = fr.getTargetPoseRobotSpace().getPosition().x; // Where it is (left-right)
            y = fr.getTargetPoseRobotSpace().getPosition().y; // Where it is (up-down)
            distance = fr.getTargetPoseRobotSpace().getPosition().z;
        }
        /*List<LLResultTypes.ColorResult> fiducials = result.getColorResults();
        int id = 0;
        for (LLResultTypes.ColorResult fiducial : fiducials) {
            double limelightx = fiducial.getTargetXDegrees(); // Where it is (left-right)
            double limelighty = fiducial.getTargetYDegrees(); // Where it is (up-down)
            double StrafeDistance_3D = fiducial.getRobotPoseTargetSpace().getPosition().y;
            botPose = fiducial.getRobotPoseFieldSpace();*/


            //telemetry.addData("Fiducial " + id, "is " + StrafeDistance_3D + " meters away");
        }

        /*LLResult aprilTagResult = limelight.getLatestResult()
        List<LLResultTypes.FiducialResult> fiducialsAprilTag = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fiducialAprilTag : fiducialsAprilTag) {
            int idAprilTag = fiducialAprilTag.getFiducialId(); // The ID number of the fiducial
            double x = fiducialAprilTag.getTargetXDegrees(); // Where it is (left-right)
            double y = fiducialAprilTag.getTargetYDegrees(); // Where it is (up-down)
            double StrafeDistance_3DZ = fiducialAprilTag.getRobotPoseTargetSpace().getPosition().z;
            double StrafeDistance_3DX = fiducialAprilTag.getRobotPoseTargetSpace().getPosition().x;
            double StrafeDistance_3DY = fiducialAprilTag.getRobotPoseTargetSpace().getPosition().y;
            //telemetry.addData("Fiducial " + id, "is " + distance + " meters away");
        }*/

    public double getAngleToGoal(){
        return mountingAngle + getTargetY();
    }

    public double getHorizontalDistance(double m_Offset){ //X
        return getTargetZ()/* + m_Offset(((goalAprilTagHeight - limelightHeight) / Math.tan(Math.toRadians(getAngleToGoal()))) + m_Offset)*/;
    }
    public double getHorizontalDistance(Follower follower){ //X
        if(limelight.getLatestResult().getStaleness() <= 0) {
            return getTargetZ();
        }
        else{
            return Math.sqrt(Math.pow(((127.7-follower.getPose().getX()) * 2.54), 2) + Math.pow(((131.7-follower.getPose().getY()) * 2.54), 2));
        }
    }

    public double getVerticalDistance(double m_Offset){ //Y
        return (goalHeightOffset) + m_Offset;
    }

    public double getHorizontalComp(){ //P
        return getHorizontalDistance(shooterOffsetX) / getArcTime();
    } //X / T

    public double getVerticalComp(){ //U
        return (getVerticalDistance(shooterOffsetY + yClearance) * 2) / getArcTime();
    } // 2Y / T

    public double getArcTime(){ //T
        return Math.pow(((getVerticalDistance(shooterOffsetY + yClearance) * 2) / gravity), 0.5);
    } //(2Y / G) ^0.5

    public double getLaunchSpeed(){ //V
        return Math.pow((gravity * Math.pow(getHorizontalDistance(shooterOffsetX), 2) / (getVerticalDistance(shooterOffsetY + yClearance) * 2)) + (2 * gravity * getVerticalDistance(shooterOffsetY + yClearance)), 0.5);
    } //((G * X^2 / 2Y) + 2YG) ^0.5

    public double getLaunchAngle() { // Q
        return Math.atan(getHorizontalComp() / getVerticalComp());
    } //atan(P / U)

    /*public double getLaunchRPM() {
        return ;
    }*/

    public void updateLimelight(){
        result = limelight.getLatestResult();
        List<LLResultTypes.FiducialResult> aprilTagResult = result.getFiducialResults();
        //loop through the list, even if there is only 1 item since it is a List type
        for (LLResultTypes.FiducialResult fr : aprilTagResult) {
            id = fr.getFiducialId();
            x = fr.getTargetPoseRobotSpace().getPosition().x; // Where it is (left-right)
            y = fr.getTargetPoseRobotSpace().getPosition().y; // Where it is (up-down)
            distance = fr.getTargetPoseRobotSpace().getPosition().z;
        }

        limeLightID = id;
        limelightTX = result.getTx();
        limelightTY = result.getTy();
        limelightTZ = distance;
        limelightPiplineType = result.getPipelineType();
        limelightTa = result.getTa();
        getTargetZ();
    }

    public void changePipeline(limelightPipelines m_pipline){
        enmLimelightPipelines = m_pipline;
        limelight.pipelineSwitch(m_pipline.value);
    }

    public double getTargetX(){
        return limelightTX;
    }

    public double getTargetY() {
        return limelightTY;
    }

    public double getTargetZ(){
        return limelightTZ * 100;
    }

    public double getID(){
        return limeLightID;
    }

    public String getPipline() {
        return limelightPiplineType;
    }

    public double getTargetArea() {
        return limelightTa;
    }
}
