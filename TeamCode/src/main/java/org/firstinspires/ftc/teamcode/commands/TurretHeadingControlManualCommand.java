package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
import org.firstinspires.ftc.teamcode.base.RobotBase;

public class TurretHeadingControlManualCommand extends CommandBase {
    RobotBase robotBase;
    Follower follower;
    double turretAngle;
    boolean isUsingManualControl;
    double manualControlPosition;
    GamepadEx launcherDriver;
    public TurretHeadingControlManualCommand(RobotBase m_robotBase, Follower m_follower, double manualPosition, GamepadEx m_launcherDriver){
        robotBase = m_robotBase;
        follower = m_follower;
        manualControlPosition = manualPosition;
        launcherDriver = m_launcherDriver;
    }

    @Override
    public void initialize(){
        launcherDriver.getGamepadButton(GamepadKeys.Button.BACK)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->isUsingManualControl = !isUsingManualControl)));
    }

    @Override
    public void execute(){
        launcherDriver.readButtons();
        if(!isUsingManualControl){
            turretAngle = robotBase.turretSubsystem.getTurretAngle(robotBase.chassisSubsystem.pinpoint, follower);
        }
        else{
            if(DataStorage.alliance == DecodeEnums.Alliance.RED){
                turretAngle = 45;
            }
            else{
                turretAngle = 275;
            }
        }
        robotBase.turretSubsystem.updatePosition(turretAngle);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}