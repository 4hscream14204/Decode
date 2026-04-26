package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.seattlesolvers.solverslib.controller.PIDFController;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;

import java.util.ArrayList;
import java.util.List;

@Configurable
public class Launcher{

    public DcMotorEx launcherMotor;
    public DcMotorEx launcherMotor2;
    public double dblTargetVel = 0;
    double maximum = 2700;
    double changeThresholdPower = 0.01;
    double changeThresholdVelocity = 3;
    int velStorageSize = 3;
    PIDFController launcherPIDF = new PIDFController(0.5,0,0,0.0004);
    List<Double> velStorage = new ArrayList<>();
    VoltageSensor voltageSensor;
    double voltage;
    double adjustedVelocity;
    double power;
    public static double proportional = 0.002;
    double error;
    double xSpeed;
    double ySpeed;
    Pose goalPose;
    double distance;
    double timeOfFlight;
    double timeOfFlightMultiplier = 0.003;
    Pose futurePose;
    double newDistance;

    public Launcher(DcMotorEx m_Launcher, DcMotorEx m_launcher2, VoltageSensor m_voltageSensor){
    launcherMotor = m_Launcher;
    launcherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    launcherMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    launcherMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    launcherMotor2 = m_launcher2;
    launcherMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    launcherMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    voltageSensor = m_voltageSensor;
    //launcherMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
    //launcherMotor.setVelocityPIDFCoefficients(9, 0.8, 0, 0.7);

    for (int i = 0; i < velStorageSize; i++) {
        velStorage.add(0.0);
    }
}

public double calculatePower(double targetVelocity){
    voltage = voltageSensor.getVoltage();
    adjustedVelocity = (targetVelocity / (voltage / 12));
    power = ((0.000380 * adjustedVelocity) + 0.057776);
    error = targetVelocity - getVelocity();
    power += (proportional * error);
    return power;
}

public void setPower(double power){
    if(Math.abs(power - launcherMotor.getPower()) > changeThresholdPower) {
        launcherMotor.setPower(power);
        launcherMotor2.setPower(power);
    }
}

public void setVelocity(double m_velocity) {
        if(m_velocity != getVelocity()) {
            dblTargetVel = m_velocity;
            setPower(calculatePower(dblTargetVel));
            velStorage.add(getVelocity());
            velStorage.remove(0);
            //dblTargetVel = m_velocity;
        }
}

public void setVelocitySimple(double m_velocity) {
    if(Math.abs(m_velocity - launcherMotor.getVelocity()) > changeThresholdVelocity) {
        launcherMotor.setVelocity(m_velocity);
        launcherMotor2.setVelocity(m_velocity);
    }
}

    /*public void setRPM(double m_RPM) {
        double m_RPMToVelocity = m_RPM / (6.28 * dblLaunchWheelRadius);
        launcherMotorLeft.setVelocity(m_RPMToVelocity);
    }*/

public double getVelocity(){
    return launcherMotor.getVelocity();
}

    /*public double getRPM() {
        return launcherMotorLeft.getVelocity() * (6.28 * dblLaunchWheelRadius);
    }*/

public void setLaunchVelocity(double m_Distance) {
    double velocity = getLaunchVelocity(m_Distance);
    setVelocity(velocity);
}

public double getLaunchVelocity(double m_Distance){
    return ((7.6943 * m_Distance) + 986.63);
}

public boolean isAtSpeed(){
    double averageSpeed = 0;
    int velStorageIndex = 0;

    for (int i = 0; i < velStorageSize; i++) {
        averageSpeed = averageSpeed + velStorage.get(velStorageIndex);
        velStorageIndex ++;
    }

    averageSpeed = averageSpeed / velStorageSize;

    return Math.abs((averageSpeed - dblTargetVel)) <= 20;
}

public double getDistance(GoBildaPinpointDriver pinpoint, Follower follower){
    if(DataStorage.alliance == DecodeEnums.Alliance.RED){
        goalPose = new Pose(144, 138);
    }
    else{
        goalPose = new Pose(144, 138).mirror();
    }
    xSpeed = pinpoint.getVelX(DistanceUnit.INCH);
    ySpeed = pinpoint.getVelY(DistanceUnit.INCH);
    distance = follower.getPose().distanceFrom(goalPose);
    timeOfFlight = distance * timeOfFlightMultiplier;
    futurePose = new Pose((goalPose.getX() - (xSpeed * timeOfFlight)), (goalPose.getY() - (ySpeed * timeOfFlight)));
    newDistance = follower.getPose().distanceFrom(futurePose);
    return newDistance;
}

public void setMaximum(double m_maximum){
    maximum = m_maximum;
}
}
