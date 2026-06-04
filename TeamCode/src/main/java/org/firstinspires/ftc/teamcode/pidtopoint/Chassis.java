package org.firstinspires.ftc.teamcode.pidtopoint;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Chassis {
    public Point currentPoint;

    private double xOffset;
    private double yOffset;
    private double headingOffset;

    private double xPIDOutput;
    private double yPIDOutput;
    public double headingPIDOutput;

    private Localizer localizer;

    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;

    double rotX;
    double rotY;

    private double dblFrontLeftPower;
    private double dblFrontRightPower;
    private double dblBackLeftPower;
    private double dblBackRightPower;

    public Chassis(Localizer m_localizer, DcMotor m_frontLeft, DcMotor m_frontRight, DcMotor m_backLeft, DcMotor m_backRight){
        localizer = m_localizer;
        frontLeftMotor = m_frontLeft;
        frontRightMotor = m_frontRight;
        backLeftMotor = m_backLeft;
        backRightMotor = m_backRight;
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void goToPoint(Point m_point){
        currentPoint = m_point;
    }

    public void update(){
        xOffset = currentPoint.getX() - localizer.getX();
        yOffset = currentPoint.getY() - localizer.getY();
        headingOffset = currentPoint.getHeadingRad() - localizer.getHeadingRad();
        if(!isAtPosition()){
            xPIDOutput = ChassisConstants.xPID.calculate(xOffset);
            yPIDOutput = ChassisConstants.yPID.calculate(yOffset);
            headingPIDOutput = ChassisConstants.headingPID.calculate(headingOffset);

            rotX = xPIDOutput * Math.cos(-localizer.getHeadingRad()) - yPIDOutput * Math.sin(-localizer.getHeadingRad());
            rotY = xPIDOutput * Math.sin(-localizer.getHeadingRad()) + yPIDOutput * Math.cos(-localizer.getHeadingRad());

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(headingPIDOutput), 1);

            dblFrontLeftPower = (rotY + rotX + headingPIDOutput) / denominator;
            dblBackLeftPower = (rotY - rotX + headingPIDOutput) / denominator;
            dblFrontRightPower = (rotY - rotX - headingPIDOutput) / denominator;
            dblBackRightPower = (rotY + rotX - headingPIDOutput) / denominator;

            frontLeftMotor.setPower(dblFrontLeftPower);
            frontRightMotor.setPower(dblFrontRightPower);
            backLeftMotor.setPower(dblBackLeftPower);
            backRightMotor.setPower(dblBackRightPower);
        }
    }

    private void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior m_zeroPowerBehavior){
        frontLeftMotor.setZeroPowerBehavior(m_zeroPowerBehavior);
        frontRightMotor.setZeroPowerBehavior(m_zeroPowerBehavior);
        backLeftMotor.setZeroPowerBehavior(m_zeroPowerBehavior);
        backRightMotor.setZeroPowerBehavior(m_zeroPowerBehavior);
    }

    public boolean isAtPosition(){
        if((Math.abs(xOffset) < 1 && Math.abs(yOffset) < 1 && Math.abs(headingOffset) < (Math.PI / 36))){
            return true;
        }
        else{
            return false;
        }
    }
}
